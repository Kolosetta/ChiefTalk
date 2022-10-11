package com.example.chieftalk

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.ActivityMainBinding
import com.example.chieftalk.models.User
import com.example.chieftalk.ui.objects.AppDrawer
import com.example.chieftalk.utilits.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

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
        initUser()
    }

    //TODO("Подумать над выносом проверки авторизации в Application")
    private fun initFunc(){
        if(AUTH.currentUser!=null){
            setSupportActionBar(toolBar)
            appDrawer.create()
        }
        else{
            replaceActivity(RegisterActivity::class.java)
        }

    }

    private fun initUser() {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.getValue(User::class.java)?.let {
                        USER = it
                    }
                }
                override fun onCancelled(error: DatabaseError) {}
            })
    }
}