package com.example.databinding

import android.util.Log
import android.view.View

/**
 *
 * @author wangzhichao
 * @since 2020/12/9
 */
class Presenter {
    fun onSaveClick(task: Task) {
        Log.d(TAG, "onSaveClick: task = $task")
    }
    
    fun onSaveClick(view: View, task: Task) {
        Log.d(TAG, "onSaveClick: view = $view, task = $task")
    }

    fun onCompletedChanged(task: Task, completed: Boolean) {
        Log.d(TAG, "onCompletedChanged: task = $task, completed = $completed")
    }

    fun onLongClicked(view: View, task: Task): Boolean {
        Log.d(TAG, "onLongClicked: view = $view, task = $task")
        return true
    }

    companion object {
        private const val TAG = "Presenter"
    }
}