package com.example.wifi

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.example.wifi.databinding.ActivityWifiP2pBinding

class WifiP2pActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWifiP2pBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiP2pBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.buttonSendFile.setOnClickListener {
            startActivity<WifiP2pSendFileActivity>()
        }
        binding.buttonReceiveFile.setOnClickListener {
            startActivity<WifiP2pReceiveFileActivity>()
        }

    }


    companion object {
        private const val TAG = "WifiP2PActivity"
    }
}