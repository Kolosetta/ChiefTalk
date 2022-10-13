package com.example.chieftalk.ui.fragments

import android.os.Bundle
import android.view.*
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentChangeUserNameBinding
import com.example.chieftalk.utilits.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class ChangeUserNameFragment : BaseChangeFragment() {

    private lateinit var binding: FragmentChangeUserNameBinding
    private var userName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(USERNAME)
        }
    }


    override fun onCreateView(inflater: LayoutInflater, cont: ViewGroup?, bundle: Bundle?): View {
        binding = FragmentChangeUserNameBinding.inflate(inflater, cont, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.settingsInputUsername.setText(userName)
    }

    override fun change(){
        validateUserName()
    }

    //Валидация нового username
    private fun validateUserName() {
        val newUserName = binding.settingsInputUsername.text.toString().lowercase(Locale.getDefault())
        if (newUserName.isEmpty()) {
            showToast(getString(R.string.settings_change_username_required))
        } else {
            REF_DATABASE_ROOT.child(NODE_USER_NAMES)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(newUserName)) {
                            showToast(getString(R.string.settings_change_username_username_exists))
                        } else {
                            changeUserName(newUserName)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {}
                })

        }
    }

    //Добавление newUserName в список юзернеймов
    private fun changeUserName(newUserName: String) {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(newUserName).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUserName(newUserName)
                }
            }
    }

    //Обновление username в сущности User
    private fun updateCurrentUserName(newUserName: String) {
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_USERNAME).setValue(newUserName)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    deleteOldUserName(newUserName)
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    //Удаление старого Username из списка юзернеймов
    private fun deleteOldUserName(newUserName: String) {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(USER.userName).removeValue()
            .addOnCompleteListener {
                if(it.isSuccessful){
                    showToast(getString(R.string.toast_data_updated))
                    USER.userName = newUserName
                    mainActivity.supportFragmentManager.popBackStack()
                } else {
                    showToast(it.exception?.message.toString())
                }
            }
    }

    companion object {
        private const val USERNAME = "username"

        @JvmStatic
        fun newInstance(userName: String) =
            ChangeUserNameFragment().apply {
                arguments = Bundle().apply {
                    putString(USERNAME, userName)
                }
            }
    }
}