package com.example.chieftalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.ActivityMainBinding
import com.example.chieftalk.ui.objects.AppDrawer
import com.example.chieftalk.utilits.replaceActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.chieftalk.utilits.AUTH
import com.example.chieftalk.utilits.initFireBase

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolBar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFireBase()
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
        if(AUTH.currentUser!=null){
            setSupportActionBar(toolBar)
            appDrawer.create()
        }
        else{
            replaceActivity(RegisterActivity::class.java)
        }

    }
}