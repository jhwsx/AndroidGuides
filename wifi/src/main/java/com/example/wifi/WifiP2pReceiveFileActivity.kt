package com.example.wifi

import android.Manifest
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pInfo
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.example.wifi.WifiP2pServerService.MyBinder
import com.example.wifi.databinding.ActivityWifiP2pReceiveFileBinding

class WifiP2pReceiveFileActivity : AppCompatActivity(), View.OnClickListener {

    private val manager: WifiP2pManager? by lazy(LazyThreadSafetyMode.NONE) {
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    private val binding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityWifiP2pReceiveFileBinding.inflate(
            layoutInflater
        )
    }

    private lateinit var channel: WifiP2pManager.Channel

    private val wifiDirectionListener = object : WifiDirectionListener {
        override fun onWifiP2pEnabled(enabled: Boolean) {
            Log.d(TAG, "onWifiP2pEnabled: $enabled")
        }

        override fun onPeersAvailable(deviceList: Collection<WifiP2pDevice>) {
            Log.d(TAG, "onPeersAvailable: ")
        }

        override fun onSelfDeviceAvailable(wifiP2pDevice: WifiP2pDevice) {
            Log.d(TAG, "onSelfDeviceAvailable: ")
        }

        override fun onDisconnection() {
            Log.d(TAG, "onDisconnection: ")
        }

        override fun onChannelDisconnected() {
            Log.d(TAG, "onChannelDisconnected: ")
        }

        override fun onConnectionInfoAvailable(info: WifiP2pInfo) {
            Log.d(
                TAG,
                "onConnectionInfoAvailable: groupFormed=${info.groupFormed}, isGroupOwner=${info.isGroupOwner}"
            )
            if (info.groupFormed && info.isGroupOwner) {
                if (wifiP2pServerService != null) {
                    startService(
                        Intent(
                            this@WifiP2pReceiveFileActivity,
                            WifiP2pServerService::class.java
                        )
                    )
                }
            }
        }

    }
    private var receiver: WifiDirectBroadcastReceiver? = null
    private var wifiP2pServerService: WifiP2pServerService? = null
    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val myBinder = service as MyBinder
            wifiP2pServerService = myBinder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
            wifiP2pServerService = null
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (manager == null) {
            finish()
            return
        }
        channel = manager!!.initialize(this, mainLooper, wifiDirectionListener)
        receiver = WifiDirectBroadcastReceiver(manager!!, channel, this, wifiDirectionListener)
        registerReceiver(receiver, WifiDirectBroadcastReceiver.intentFilter)
        binding.btnCreateGroup.setOnClickListener(this)
        binding.btnRemoveGroup.setOnClickListener(this)
        bindService(
            Intent(this, WifiP2pServerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
        if (wifiP2pServerService != null) {
            unbindService(serviceConnection)
        }
        stopService(Intent(this, WifiP2pServerService::class.java))
    }

    override fun onClick(v: View) {
        when (v) {
            binding.btnCreateGroup -> createGroup()
            binding.btnRemoveGroup -> removeGroup()
        }
    }

    private fun removeGroup() {
        manager?.removeGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "removeGroup onSuccess: ")
            }

            override fun onFailure(reason: Int) {
                Log.d(TAG, "removeGroup onFailure: $reason")
            }
        })
    }

    private fun createGroup() {
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
            return
        }
        manager?.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                Log.d(TAG, "createGroup onSuccess: ")
            }

            override fun onFailure(reason: Int) {
                Log.e(TAG, "createGroup onFailure: reason = $reason")
            }
        })
    }

    companion object {
        private const val TAG = "WifiP2pReceiveFileActiv"
    }
}