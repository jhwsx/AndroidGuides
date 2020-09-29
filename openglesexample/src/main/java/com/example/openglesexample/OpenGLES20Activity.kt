package com.example.openglesexample

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class OpenGLES20Activity : AppCompatActivity() {
    private lateinit var glView: MyGLSurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        glView = MyGLSurfaceView(this)
        setContentView(glView)
    }

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val starter = Intent(context, OpenGLES20Activity::class.java)
            context.startActivity(starter)
        }
    }
}