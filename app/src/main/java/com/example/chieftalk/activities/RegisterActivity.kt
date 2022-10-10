package com.example.chieftalk.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.chieftalk.R
import com.example.chieftalk.databinding.ActivityRegisterBinding
import com.example.chieftalk.ui.fragments.EnterPhoneFragment
import com.example.chieftalk.utilits.initFireBase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var toolBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFireBase()
    }

    override fun onStart() {
        super.onStart()
        toolBar = binding.registerToolbar
        setSupportActionBar(toolBar)
        title = getString(R.string.register_title_your_phone)
        supportFragmentManager.beginTransaction()
            .add(R.id.registerContainer, EnterPhoneFragment.newInstance())
            .commit()
    }
}