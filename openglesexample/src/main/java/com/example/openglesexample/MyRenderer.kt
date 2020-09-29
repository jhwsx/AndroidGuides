package com.example.openglesexample

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * 构建渲染程序类
 *
 * @author wangzhichao
 * @date 20-9-29
 */
class MyRenderer : GLSurfaceView.Renderer {
    // 调用一次以设置视图的 OpenGL ES 环境
    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        // 设置背景帧的颜色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
    }
    // 当视图的几何图形发生变化（例如当设备的屏幕方向发生变化）时调用
    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)
    }

    // 每次重新绘制视图时调用。
    override fun onDrawFrame(gl: GL10?) {
        // 重绘背景色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
    }
}