package com.example.chieftalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.chieftalk.databinding.ActivityMainBinding
import com.example.chieftalk.ui.objects.AppDrawer

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolBar: Toolbar
    private lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onStart() {
        super.onStart()
        initFields()
        initFunc()
    }

    private fun initFields(){
        toolBar = binding.mainToolbar
        appDrawer = AppDrawer(this, toolBar)
    }

    private fun initFunc(){
        setSupportActionBar(toolBar)
        appDrawer.create()
    }
}