package com.example.wifi

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.example.wifi.databinding.ProgressDialogBinding

/**
 *
 * @author wangzhichao
 * @since 2021/3/11
 */
class ProgressDialog(context: Context, private val fileName: String) : AlertDialog(context) {
    private val binding = ProgressDialogBinding.inflate(layoutInflater)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.tvTitle.text = fileName

    }

    fun setProgress(progress: Int) {
        binding.pb.progress = progress
    }
}