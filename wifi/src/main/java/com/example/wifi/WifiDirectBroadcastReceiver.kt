package com.example.wifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.NetworkInfo
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pDeviceList
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

/**
 *
 * @author wangzhichao
 * @since 2021/3/10
 */
class WifiDirectBroadcastReceiver(
    private val manager: WifiP2pManager,
    private val channel: WifiP2pManager.Channel,
    private val activity: AppCompatActivity,
    private val listener: WifiDirectionListener,
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (context == null || intent == null) return
        when (intent.action) {
            // 当 WLAN P2P 在设备上启用或停用时广播。
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                // Check to see if Wi-Fi is enabled and notify appropriate activity
                Log.d(TAG, "onReceive: 当 WLAN P2P 在设备上启用或停用时广播")
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                when (state) {
                    WifiP2pManager.WIFI_P2P_STATE_ENABLED -> {
                        Log.d(TAG, "onReceive: Wifi P2P is enabled")
                        listener.onWifiP2pEnabled(true)
                        manager.requestDeviceInfo(channel) {
                            Log.d(TAG, "onReceive: requestDeviceInfo, onDeviceInfoAvailable: $it")
                            it?.let { it1 -> listener.onSelfDeviceAvailable(it1) }
                        }
                    }
                    else -> {
                        Log.d(TAG, "onReceive: Wifi P2P is not enabled")
                        listener.onWifiP2pEnabled(false)
                    }
                }
            }
            // 当您调用 discoverPeers() 时广播。如果您在应用中处理此 Intent，则通常需要调用 requestPeers() 以获取对等设备的更新列表。
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> {
                // Call WifiP2pManager.requestPeers() to get a list of current peers
                Log.d(TAG, "onReceive: 当您调用 discoverPeers() 时广播。")
                if (ActivityCompat.checkSelfPermission(
                        activity,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    manager.requestPeers(channel) { peers: WifiP2pDeviceList? ->
                        peers?.let {
                            val deviceList = it.deviceList
                            Log.d(
                                TAG,
                                "onReceive: requestPeers, deviceList size = ${deviceList.size}"
                            )
                            listener.onPeersAvailable(deviceList = deviceList)
                            for (wifiP2pDevice in deviceList) {
                                Log.d(TAG, "onReceive: requestPeers, $wifiP2pDevice")
                            }
                        }
                    }
                }
            }
            // 当设备的 Wifi P2p 连接状态更改时广播。
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                // Respond to new connection or disconnections
                Log.d(TAG, "onReceive: 当设备的 WLAN 连接状态更改时广播。")
                val networkInfo =
                    intent.getParcelableExtra<NetworkInfo>(WifiP2pManager.EXTRA_NETWORK_INFO)
                if (networkInfo != null && networkInfo.isConnected) {
                    manager.requestConnectionInfo(channel) {
                        Log.d(
                            TAG,
                            "onReceive: requestConnectionInfo onConnectionInfoAvailable, $it"
                        )
                        listener.onConnectionInfoAvailable(it)
                    }
                    Log.d(TAG, "onReceive: 已连接 P2p 设备：networkInfo=$networkInfo")

                } else {
                    listener.onDisconnection()
                }
            }
            // 当设备的详细信息（例如设备名称）更改时广播。
            WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION -> {
                // Respond to this device's wifi state changing
                Log.d(TAG, "onReceive: 当设备的详细信息（例如设备名称）更改时广播。")
                val wifiP2pDevice =
                    intent.getParcelableExtra<WifiP2pDevice>(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE)
                Log.d(TAG, "onReceive: $wifiP2pDevice")
                wifiP2pDevice?.let { listener.onSelfDeviceAvailable(it) }
            }
        }
    }

    companion object {
        private const val TAG = "WifiDirectReceiver"
        val intentFilter = IntentFilter().apply {
            addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
            addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
        }
    }
}