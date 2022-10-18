package com.example.chieftalk.utilits

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.provider.ContactsContract
import android.util.Log
import com.example.chieftalk.models.ContactUser
import com.example.chieftalk.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

lateinit var AUTH: FirebaseAuth
lateinit var UID: String
lateinit var REF_DATABASE_ROOT: DatabaseReference
lateinit var REF_STORAGE_ROOT: StorageReference
lateinit var USER: User

const val NODE_USERS = "users"
const val NODE_USER_NAMES = "usernames"
const val NODE_PHONES = "phones"
const val NODE_PHONE_CONTACTS = "phone_contacts"

const val FOLDER_PROFILE_IMAGE = "profile_images"

const val CHILD_ID = "id"
const val CHILD_PHONE = "phone"
const val CHILD_USERNAME = "username"
const val CHILD_FULLNAME = "fullname"
const val CHILD_BIO = "bio"
const val CHILD_PHOTO_URL = "photoUrl"
const val CHILD_STATE = "state"

fun initFireBase() {
    AUTH = FirebaseAuth.getInstance()
    REF_DATABASE_ROOT = FirebaseDatabase.getInstance().reference
    REF_STORAGE_ROOT = FirebaseStorage.getInstance().reference
    USER = User()
    UID = AUTH.currentUser?.uid.toString()
}

inline fun initUser(crossinline func: () -> Unit) {
    REF_DATABASE_ROOT.child(NODE_USERS).child(UID)
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.getValue(User::class.java)?.let {
                    USER = it
                    if (USER.userName.isEmpty()) {
                        USER.userName = UID
                    }
                    //TODO временно решение, чтобы юзер точно инициалиировался раньше всего остального
                    //Заменить потом корутинами
                    func()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
}

@SuppressLint("Range")
//Записывает в лист список всех локальных контактов из бд контактов курсором
fun initContacts(activity: Activity) {
    if (checkPermission(activity, READ_CONTACT)) {
        val arrayContacts = arrayListOf<ContactUser>()
        val cursor = activity.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val displayName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                        .replace(Regex("[\\s-()]"), "").padStart(12, '+')
                val newContact = ContactUser(fullName = displayName, phone = phone)
                arrayContacts.add(newContact)
            }
        }
        cursor?.close()
        updatePhoneToDatabase(arrayContacts)
    }
}

//Добавляет в базу Fb все контакты, зарегестрирвоанные в приложении
fun updatePhoneToDatabase(arrayContacts: ArrayList<ContactUser>) {
    REF_DATABASE_ROOT.child(NODE_PHONES).addListenerForSingleValueEvent(object : ValueEventListener{
        override fun onDataChange(snapshot: DataSnapshot) {
            snapshot.children.forEach{snap ->
                arrayContacts.forEach{ contact ->
                    if(snap.key == contact.phone){
                        REF_DATABASE_ROOT.child(NODE_PHONE_CONTACTS).child(UID)
                            .child(snap.value.toString())
                            .child(CHILD_ID)
                    }
                }
            }
        }

        override fun onCancelled(error: DatabaseError) {}
    })
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