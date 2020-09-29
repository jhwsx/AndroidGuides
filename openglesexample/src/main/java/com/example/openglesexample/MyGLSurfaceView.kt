package com.example.openglesexample

import android.content.Context
import android.opengl.GLSurfaceView

/**
 * 构建 GLSurfaceView 对象
 *
 * @author wangzhichao
 * @date 20-9-29
 */
class MyGLSurfaceView(context: Context?) : GLSurfaceView(context) {
    private val renderer: MyRenderer
    init {
        // 创建一个 OpenGL ES 2.0 的环境
        setEGLContextClientVersion(2)

        renderer = MyRenderer()
        // 设置 Renderer，用于在 GLSurfaceView 上绘制
        setRenderer(renderer)
    }
}