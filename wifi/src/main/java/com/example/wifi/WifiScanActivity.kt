package com.example.wifi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.wifi.databinding.ActivityWifiScanBinding

class WifiScanActivity : AppCompatActivity() {
    private lateinit var wifiManager: WifiManager
    private lateinit var binding: ActivityWifiScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWifiScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        binding.btnScanResults.setOnClickListener {
            val wifiScanReceiver = object: BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (context == null || intent == null) return
                    val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                    if (success) {
                        scanSuccess()
                    } else {
                        scanFailure()
                    }
                }
            }
            val intentFilter = IntentFilter().apply {
                addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
            }
            applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
            val success = wifiManager.startScan()
            if (!success) {
                scanFailure()
            }
        }

    }

    private fun scanFailure() {
        val scanResults = wifiManager.scanResults
        Log.d(TAG, "scanFailure: ")
        for (scanResult in scanResults) {
            Log.d(TAG, "$scanResult")
        }

    }

    private fun scanSuccess() {
        val scanResults: List<ScanResult> = wifiManager.scanResults
        Log.d(TAG, "scanSuccess: ")
        for (scanResult in scanResults) {
            Log.d(TAG, "$scanResult")
        }
    }

    companion object {
        private const val TAG = "WLANScanActivity"
    }
}