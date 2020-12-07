package com.example.databindingexamples

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.databindingexamples.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tv.text = "I'm in activity"
        if (supportFragmentManager.findFragmentById(R.id.fl) == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fl, MainFragment.newInstance())
                .commit()
        }
        val str: String = ""
        str.isBlank()
    }
}