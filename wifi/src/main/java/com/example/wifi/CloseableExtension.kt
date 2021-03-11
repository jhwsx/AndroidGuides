package com.example.wifi

import java.io.Closeable
import java.io.IOException

/**
 *
 * @author wangzhichao
 * @since 2021/3/11
 */
fun Closeable?.closeQuietly() {
    try {
        this?.close()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}