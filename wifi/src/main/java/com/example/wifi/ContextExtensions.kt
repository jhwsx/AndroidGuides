package com.example.wifi

import android.content.Context
import android.content.Intent

/**
 *
 * @author wangzhichao
 * @since 2021/3/10
 */
inline fun <reified T> Context.startActivity() {
    val intent = Intent(this, T::class.java)
    startActivity(intent)
}