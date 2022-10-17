package com.example.chieftalk

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.ActivityMainBinding
import com.example.chieftalk.ui.fragments.SettingsFragment
import com.example.chieftalk.ui.objects.AppDrawer
import com.example.chieftalk.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var toolBar: Toolbar
    lateinit var appDrawer: AppDrawer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFireBase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts(this@MainActivity)
            }
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

    //Срабатывает при изменении какого-либо permission
    override fun onRequestPermissionsResult(rcode: Int, perm: Array<out String>, result: IntArray) {
        super.onRequestPermissionsResult(rcode, perm, result)
        if (ContextCompat.checkSelfPermission(this, READ_CONTACT) == PackageManager.PERMISSION_GRANTED) {
            initContacts(this)
        }
    }
}