package com.example.chieftalk.utilits

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


const val READ_CONTACT = Manifest.permission.READ_CONTACTS
const val PERMISSION_REQUEST = 200

//Возращает bool наличия разрешения и отправляет запрос на разрешение пользователю
fun checkPermission(activity: Activity, permission: String): Boolean{
    return if(ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED){
        ActivityCompat.requestPermissions(activity, arrayOf(permission), PERMISSION_REQUEST)
        false
    } else true
}