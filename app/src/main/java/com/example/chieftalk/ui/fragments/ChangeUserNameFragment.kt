package com.example.chieftalk.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.chieftalk.MainActivity
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentChangeUserNameBinding
import com.example.chieftalk.utilits.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import java.util.*

class ChangeUserNameFragment : Fragment() {

    private lateinit var binding: FragmentChangeUserNameBinding
    private lateinit var mainActivity: MainActivity
    private var userName: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            userName = it.getString(USERNAME)
        }
    }

    override fun onStart() {
        super.onStart()
        mainActivity.appDrawer.disableDrawer()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        mainActivity.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> validateUserName()
        }
        return true
    }

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

    private fun changeUserName(newUserName: String) {
        REF_DATABASE_ROOT.child(NODE_USER_NAMES).child(newUserName).setValue(UID)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    updateCurrentUserName(newUserName)
                }
            }
    }

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