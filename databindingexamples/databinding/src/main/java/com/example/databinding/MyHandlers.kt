package com.example.databinding

import android.view.View
import android.widget.Toast

/**
 *
 * @author wangzhichao
 * @since 2020/12/8
 */
class MyHandlers {
    fun onClickFriend(view: View) {
        Toast.makeText(view.context, "onClickFriend:", Toast.LENGTH_SHORT).show()
    }
}