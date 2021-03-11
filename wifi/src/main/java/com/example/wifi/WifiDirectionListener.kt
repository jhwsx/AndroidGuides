package com.example.wifi

import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager

/**
 *
 * @author wangzhichao
 * @since 2021/3/10
 */
interface WifiDirectionListener : WifiP2pManager.ChannelListener, WifiP2pManager.ConnectionInfoListener {
    fun onWifiP2pEnabled(enabled: Boolean)

    fun onPeersAvailable(deviceList: Collection<WifiP2pDevice>)

    fun onSelfDeviceAvailable(wifiP2pDevice: WifiP2pDevice)

    fun onDisconnection()
}