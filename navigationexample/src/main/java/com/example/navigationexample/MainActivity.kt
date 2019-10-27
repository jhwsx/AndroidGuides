package com.example.navigationexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import com.example.navigationexample.databinding.MainActivityBinding

class MainActivity : AppCompatActivity(), TitleFragment.OptionItemsSelectCallback {


    private lateinit var binding: MainActivityBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<MainActivityBinding>(this, R.layout.main_activity)
        setupToolbar()
        navController = findNavController(R.id.navHostFragment)
        drawerLayout = binding.drawerLayout
        // 设置这一行，才会有 Up 导航
//        NavigationUI.setupActionBarWithNavController(this, navController)
        // 设置这一行，才会有汉堡包出现
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        // 设置这一行，就支持了 NavigationView 里的 item 导航
        NavigationUI.setupWithNavController(binding.navView, navController)

    }

    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp()
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return false
        } else {
            return navController.navigateUp(drawerLayout)
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()

    }

    override fun onOptionItemsSelected() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
    }
    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
    }
}
