package com.example.databinding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.databinding.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val binding: MainActivityBinding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        // 等价于下面两行的写法
        val binding: MainActivityBinding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.user = User(firstName = "Test", lastName = "User")
//        binding.user = null
        // 集合
        binding.list = arrayListOf("A")
        binding.map = hashMapOf("a" to "1")
        // 资源
        // Method references
        // 方法引用的表达式是在编译期间处理的。
        // The major difference between method references and listener bindings is that the actual
        // listener implementation is created when the data is bound, not when the event is triggered.
        // If you prefer to evaluate the expression when the event happens, you should use listener binding.

        // Note: The signature of the method in the expression must exactly match the signature of
        // the method in the listener object. 表达式中的方法名必须完全匹配监听对象中的方法名。
        binding.handlers = MyHandlers()

        // Listener bindings
        // In listener bindings, only your return value must match the expected return value of the
        // listener (unless it is expecting void).
        binding.task = Task("taskA")
        binding.presenter = Presenter()
    }
}