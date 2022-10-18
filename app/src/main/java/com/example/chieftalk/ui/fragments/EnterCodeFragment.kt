package com.example.chieftalk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chieftalk.MainActivity
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.FragmentEnterCodeBinding
import com.example.chieftalk.utilits.*
import com.google.firebase.auth.PhoneAuthProvider


class EnterCodeFragment : Fragment() {

    private lateinit var binding: FragmentEnterCodeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterCodeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onStart() {
        super.onStart()
        val id = requireArguments().getString(ID) ?: ERROR
        val phoneNumber = requireArguments().getString(PHONE_NUMBER) ?: ERROR
        binding.registerInputCode.addTextChangedListener(AppTextWatcher {
            if (it.toString().length >= 6) {
                enterCode(id, it.toString(), phoneNumber)
            }
        })
    }

    private fun enterCode(id: String, code: String, phoneNumber: String) {
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential)
            .addOnSuccessListener {
                val uid = AUTH.currentUser?.uid.toString()
                val dataMap = mutableMapOf<String, Any>()
                dataMap[CHILD_ID] = uid
                dataMap[CHILD_PHONE] = phoneNumber
                dataMap[CHILD_USERNAME] = uid
                REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                    .addOnSuccessListener {
                        REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dataMap)
                            .addOnSuccessListener {
                                showToast("Welcome")
                                (requireActivity() as RegisterActivity).replaceActivity(MainActivity::class.java)
                            }
                            .addOnFailureListener {
                                showToast(it.message.toString())
                            }

                    }
            }
            .addOnFailureListener {
                showToast(it.message.toString())
            }
    }

    companion object {
        private const val ID = "ID"
        private const val PHONE_NUMBER = "PHONE_NUMBER"
        private const val ERROR = "ERROR"

        @JvmStatic
        fun newInstance(id: String, phoneNumber: String) =
            EnterCodeFragment().apply {
                arguments = Bundle().apply {
                    putString(ID, id)
                    putString(PHONE_NUMBER, phoneNumber)
                }
            }
    }
}