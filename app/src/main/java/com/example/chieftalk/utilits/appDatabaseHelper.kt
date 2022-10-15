package com.example.chieftalk.utilits

import android.net.Uri
import android.util.Log
import com.example.chieftalk.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User

const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"

const val FOLDER_PROFILE_IMAGE = "profile_images"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"

fun initFireBase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun putImageToStorage(uri: Uri, path: StorageReference, crossinline func: () -> Unit) {
    path.putFile(uri)
        .addOnSuccessListener { func() }
        .addOnFailureListener { Log.i("Error", it.message.toString()) }
}

inline fun getUrlFromStorage(path: StorageReference, crossinline func: (url: String) -> Unit) {
    path.downloadUrl
        .addOnSuccessListener { func(it.toString()) }
        .addOnFailureListener { Log.i("Error", it.message.toString()) }
}

inline fun putUrlToDB(photoUrl: String, crossinline func: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_PHOTO_URL).setValue(photoUrl)
        .addOnSuccessListener { func() }
        .addOnFailureListener { Log.i("Error", it.message.toString()) }
}