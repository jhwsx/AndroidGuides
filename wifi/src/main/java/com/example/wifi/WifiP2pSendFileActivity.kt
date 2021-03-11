package com.example.wifi

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.*
import com.example.wifi.WifiDirectBroadcastReceiver.Companion.intentFilter
import com.example.wifi.databinding.ActivityWifiP2pSendFileBinding
import com.example.wifi.databinding.WifiP2pDeviceRecycleItemBinding
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import com.zhihu.matisse.internal.entity.CaptureStrategy
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileInputStream
import java.io.ObjectOutputStream
import java.io.OutputStream
import java.net.InetSocketAddress
import java.net.Socket

class WifiP2pSendFileActivity : AppCompatActivity() {
    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }
    var channel: WifiP2pManager.Channel? = null
    var receiver: BroadcastReceiver? = null
    private var clickedWifiP2pDevice: WifiP2pDevice? = null
    private var wifiP2pInfo: WifiP2pInfo? = null

    @SuppressLint("MissingPermission")
    val adapter: RecyclerViewListAdapter = RecyclerViewListAdapter {
        clickedWifiP2pDevice = it
        val config = WifiP2pConfig().apply {
            wps.setup = WpsInfo.PBC
            deviceAddress = it.deviceAddress
        }
        channel?.let {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                manager?.connect(it, config, object : WifiP2pManager.ActionListener {
                    override fun onSuccess() {
                        Log.d(TAG, "connect onSuccess: ")
                    }

                    override fun onFailure(reason: Int) {
                        Log.d(TAG, "connect onFailure: $reason")
                    }
                })
            }
        }
    }

    private lateinit var binding: ActivityWifiP2pSendFileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiP2pSendFileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        channel = manager?.initialize(this, mainLooper, null)
        channel?.let { ch ->
            receiver =
                WifiDirectBroadcastReceiver(manager!!, ch, this, object : WifiDirectionListener {
                    override fun onWifiP2pEnabled(enabled: Boolean) {

                    }

                    override fun onPeersAvailable(deviceList: Collection<WifiP2pDevice>) {
                        val list = mutableListOf<WifiP2pDevice>()
                        list.addAll(deviceList)
                        adapter.submitList(list)
                        binding.buttonDisconnect.isEnabled =
                            list.any { it.status == WifiP2pDevice.CONNECTED }
                        binding.buttonChooseFile.isEnabled = binding.buttonDisconnect.isEnabled
                    }

                    override fun onChannelDisconnected() {
                        Log.d(TAG, "onChannelDisconnected: ")
                    }

                    override fun onConnectionInfoAvailable(info: WifiP2pInfo?) {
                        if (info == null) {
                            binding.tvConnectedDevice.text = ""
                        } else {
                            binding.tvConnectedDevice.text = with(StringBuffer()) {
                                append("连接的设备信息：\n")
                                append("连接的设备名称：${clickedWifiP2pDevice?.deviceName}\n")
                                append("连接的设备地址：${clickedWifiP2pDevice?.deviceAddress}\n")
                                append("连接的设备是否是群主：${info.isGroupOwner}\n")
                                append("连接的设备是否是 groupFormed：${info.groupFormed}\n")
                                append("群主地址：${info.groupOwnerAddress}\n")
                            }
                            if (info.groupFormed && !info.isGroupOwner) {
                                wifiP2pInfo = info
                            }
                        }
                    }

                    override fun onSelfDeviceAvailable(wifiP2pDevice: WifiP2pDevice) {
                        binding.tvThisDevice.text = with(StringBuffer()) {
                            append("本设备信息：\n")
                            append("设备名称：${wifiP2pDevice.deviceName}\n")
                            append("设备地址：${wifiP2pDevice.deviceAddress}\n")
                            append("连接状态：${wifiP2pDevice.status.getDeviceStringStatus()}\n")
                            toString()
                        }
                    }

                    override fun onDisconnection() {
                        wifiP2pInfo = null
                    }
                })
        }
        binding.buttonDiscoverPeers.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    0
                )
                return@setOnClickListener
            }
            manager?.discoverPeers(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d(TAG, "discoverPeers, onSuccess: ")
                }

                override fun onFailure(reason: Int) {
                    Log.d(TAG, "discoverPeers, onFailure: $reason")
                }
            })
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            ).apply {
                setDrawable(
                    ContextCompat.getDrawable(
                        this@WifiP2pSendFileActivity,
                        R.drawable.divider
                    )!!
                )
            })
        binding.recyclerView.adapter = adapter
        binding.buttonDisconnect.setOnClickListener {
            manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
                override fun onSuccess() {
                    Log.d(TAG, "removeGroup onSuccess: ")
                    binding.tvConnectedDevice.text = ""
                }

                override fun onFailure(reason: Int) {
                    Log.d(TAG, "removeGroup onFailure: $reason")
                }
            })
        }
        binding.buttonChooseFile.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
                )
                return@setOnClickListener
            }
            try {
                Matisse.from(this)
                    .choose(MimeType.ofImage())
                    .countable(true)
                    .showSingleMediaType(true)
                    .maxSelectable(1)
                    .capture(false)
                    .captureStrategy(
                        CaptureStrategy(
                            true,
                            BuildConfig.APPLICATION_ID + ".fileprovider"
                        )
                    )
                    .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                    .thumbnailScale(0.70f)
                    .imageEngine(Glide4Engine())
                    .forResult(CODE_CHOOSE_FILE)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        receiver?.let {
            registerReceiver(it, intentFilter)
        }
    }

    override fun onPause() {
        super.onPause()
        receiver?.let {
            unregisterReceiver(it)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_CHOOSE_FILE) {
            val pathList: List<String>? = Matisse.obtainPathResult(data)
            Log.d(TAG, "onActivityResult: ${pathList?.joinToString()}")
            if (!pathList.isNullOrEmpty()) {
                val file = File(pathList[0])
                // 传输文件给服务端
                transferFile(file)
            }
        }
    }

    private fun transferFile(file: File) {
        if (!file.exists()) {
            Log.d(TAG, "transferFile: return 1")
            return
        }
        if (wifiP2pInfo == null) {
            Log.d(TAG, "transferFile: return 2")
            return
        }
        val fileTransfer = FileTransfer(file.path, file.length())
        val hostname = wifiP2pInfo!!.groupOwnerAddress.hostAddress
        Observable.create(ObservableOnSubscribe<Int> {
            fileTransfer.md5 = Md5Util.getMd5(File(fileTransfer.filePath))
            Log.d(TAG, "transferFile: 文件的 MD5 值是：${fileTransfer.md5}")
            // 客户端发起 Socket 连接
            var socket: Socket? = null
            var objectOutputStream: ObjectOutputStream? = null
            var outputStream: OutputStream? = null
            var fileInputStream: FileInputStream? = null
            try {
                socket = Socket()
                socket.bind(null)
                socket.connect(InetSocketAddress(hostname, PORT), 10000)
                outputStream = socket.getOutputStream()
                objectOutputStream = ObjectOutputStream(outputStream)
                // 传输文件信息对象
                objectOutputStream.writeObject(fileTransfer)
                // 传输文件内容
                fileInputStream = FileInputStream(File(fileTransfer.filePath))
                val fileLength = fileTransfer.fileLength
                var total = 0L
                val buffer = ByteArray(512)
                var length = fileInputStream.read(buffer)
                while (length != -1) {
                    outputStream.write(buffer, 0, length)
                    total += length
                    val progress = (total * 100 / fileLength).toInt()
                    if (!it.isDisposed) {
                        it.onNext(progress)
                    }
                    Log.d(TAG, "transferFile: 文件发送进度：$progress")
                    length = fileInputStream.read(buffer)
                }
                if (it.isDisposed) return@ObservableOnSubscribe
                it.onComplete()
            } catch (e: Exception) {
                if (it.isDisposed) return@ObservableOnSubscribe
                it.onError(e)
            } finally {
                // 关闭 Socket 和流
                if (socket != null && !socket.isClosed) {
                    socket.close()
                }
                fileInputStream?.close()
                outputStream?.close()
                objectOutputStream?.close()
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Int> {
                private var progressDialog: ProgressDialog? = null

                override fun onSubscribe(d: Disposable) {

                }

                override fun onNext(progress: Int) {
                    if (progressDialog == null) {
                        progressDialog = ProgressDialog(this@WifiP2pSendFileActivity, file.name)
                        progressDialog!!.show()
                    }

                    progressDialog!!.setProgress(progress)
                }

                override fun onError(e: Throwable) {
                    Log.e(TAG, "onError: ", e)
                    progressDialog?.dismiss()
                }

                override fun onComplete() {
                    progressDialog?.dismiss()
                }
            })
    }

    companion object {
        private const val TAG = "WifiP2pSendFileActivity"
        private const val CODE_CHOOSE_FILE = 100
    }

}