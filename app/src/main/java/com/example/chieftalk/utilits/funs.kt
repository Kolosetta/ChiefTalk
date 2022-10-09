package com.example.chieftalk.utilits

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Fragment.showToast(message: String){
    Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.replaceActivity(activity: Class<out AppCompatActivity>){
    val intent = Intent(this, activity)
    startActivity(intent)
    this.finish()
}