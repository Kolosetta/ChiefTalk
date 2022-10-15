package com.example.chieftalk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.ActivityMainBinding
import com.example.chieftalk.models.User
import com.example.chieftalk.ui.fragments.SettingsFragment
import com.example.chieftalk.ui.objects.AppDrawer
import com.example.chieftalk.utilits.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolBar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFireBase()
        initUser{
            initFields()
            initFunc()
        }
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)
    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    private fun initFields() {
        toolBar = binding.mainToolbar
        appDrawer = AppDrawer(this, toolBar)
    }

    //TODO("Подумать над выносом проверки авторизации в Application")
    private fun initFunc() {
        if (AUTH.currentUser != null) {
            setSupportActionBar(toolBar)
            println(USER.fullName)
            appDrawer.create()
        } else {
            replaceActivity(RegisterActivity::class.java)
        }
    }



    //Вызывается как результат CropPhoto Activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == Activity.RESULT_OK
            && data != null
        ) {
            (supportFragmentManager.findFragmentByTag("SETTINGS_FRAGMENT") as SettingsFragment)
                .uploadUserPhoto(data)
        }
    }

}