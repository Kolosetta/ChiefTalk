package com.example.chieftalk.models

import com.google.firebase.database.PropertyName

data class ContactUser(
    @PropertyName("fullname")
    var fullName: String = "",
    @PropertyName("phone")
    var phone: String = "",
)
