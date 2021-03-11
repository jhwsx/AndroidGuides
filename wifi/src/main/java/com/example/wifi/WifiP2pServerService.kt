package com.example.wifi

import android.app.IntentService
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.ObjectInputStream
import java.net.InetSocketAddress
import java.net.ServerSocket
import java.net.Socket

/**
 *
 * @author wangzhichao
 * @since 2021/3/11
 */
class WifiP2pServerService : IntentService(TAG) {

    private var serverSocket: ServerSocket? = null
    private var fileOutputStream: FileOutputStream? = null
    private var inputStream: InputStream? = null
    private var objectInputStream: ObjectInputStream? = null


    inner class MyBinder : Binder() {
        fun getService(): WifiP2pServerService {
            return this@WifiP2pServerService
        }
    }
    override fun onBind(intent: Intent?): IBinder {
        return MyBinder()
    }

    override fun onHandleIntent(intent: Intent?) {
        clean()
        try {
            serverSocket = ServerSocket()
            serverSocket!!.reuseAddress = true
            serverSocket!!.bind(InetSocketAddress(PORT))
            val client: Socket = serverSocket!!.accept()
            Log.d(TAG, "onHandleIntent: 客户端IP地址 : " + client.inetAddress.hostAddress)
            inputStream = client.getInputStream()
            // 取出文件信息对象
            objectInputStream = ObjectInputStream(inputStream)
            val fileTransfer = objectInputStream!!.readObject() as FileTransfer
            Log.d(TAG, "onHandleIntent: 待接收的文件：$fileTransfer")
            // 开始接收文件存放到指定位置
            val name = File(fileTransfer.filePath).name
            val storedFile = File(applicationContext.getExternalFilesDir(null), name)
            fileOutputStream = FileOutputStream(storedFile)
            val buffer = ByteArray(512)
            var total = 0L
            val fileLength = fileTransfer.fileLength
            var length = inputStream!!.read(buffer)
            while (length != -1) {
                fileOutputStream!!.write(buffer, 0, length)
                total += length
                val progress = (total * 100 / fileLength).toInt()
                Log.d(TAG, "onHandleIntent: 文件接收进度：$progress")
                length = inputStream!!.read(buffer)
            }
            Log.d(TAG, "onHandleIntent: 文件接收成功，文件的 MD5 值是：${Md5Util.getMd5(storedFile)}")
        } catch (e: Exception) {
            Log.e(TAG, "onHandleIntent: 文件接收失败", e)
        } finally {
            clean()
        }
        // 再次启动服务，等待客户端下次连接
        startService(Intent(this, WifiP2pServerService::class.java))
    }

    override fun onDestroy() {
        super.onDestroy()
        clean()
    }

    private fun clean() {
        if (serverSocket != null && !serverSocket!!.isClosed) {
            try {
                serverSocket!!.close()
                serverSocket = null
            } catch (e: Exception) {
            }
        }
        fileOutputStream.closeQuietly()
        inputStream.closeQuietly()
        objectInputStream.closeQuietly()
    }

    companion object {
        private const val TAG = "WifiP2pServerService"
    }
}