package com.example.databindingexamples

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
/**
 * 所有序列化的属性都要声明在主构造器里面
 * @author wangzhichao
 * @since 2020/12/7
 */
@Parcelize
class User(val firstName: String, val lastName: String, val age: Int): Parcelable