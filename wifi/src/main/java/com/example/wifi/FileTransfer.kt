package com.example.wifi

import java.io.Serializable

/**
 *
 * @author wangzhichao
 * @since 2021/3/11
 */
class FileTransfer(val filePath: String, val fileLength: Long): Serializable {
    var md5: String = ""

    companion object {
        private const val serialVersionUID: Long = 1L
    }
}
