package com.example.chieftalk.models

import com.google.firebase.database.PropertyName

data class User(
    @PropertyName("id")
    val id: String = "",
    @PropertyName("username")
    var userName: String = "",
    @PropertyName("bio")
    var bio: String = "",
    @PropertyName("fullname")
    var fullName: String = "",
    @PropertyName("state")
    var state: String = "",
    @PropertyName("phone")
    var phone: String = "",
    @PropertyName("photoUrl")
    var photoUrl: String = "empty"
)
