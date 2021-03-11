package com.example.wifi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wifi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnScanResults.setOnClickListener {
            startActivity<WifiScanActivity>()
        }
        binding.btnWifiP2p.setOnClickListener {
            startActivity<WifiP2pActivity>()
        }
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}