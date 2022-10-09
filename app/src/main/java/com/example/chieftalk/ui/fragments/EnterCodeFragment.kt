package com.example.chieftalk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chieftalk.MainActivity
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.FragmentEnterCodeBinding
import com.example.chieftalk.utilits.AUTH
import com.example.chieftalk.utilits.AppTextWatcher
import com.example.chieftalk.utilits.replaceActivity
import com.example.chieftalk.utilits.showToast
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
        val id = requireArguments().getString(ID) ?: "error"
        binding.registerInputCode.addTextChangedListener(AppTextWatcher {
            if (it.toString().length >= 6) {
                enterCode(id, it.toString())
            }
        })
    }

    private fun enterCode(id: String, code: String) {
        val credential = PhoneAuthProvider.getCredential(id, code)
        AUTH.signInWithCredential(credential).addOnCompleteListener{
            if(it.isSuccessful){
                showToast("Welcome")
                (requireActivity() as RegisterActivity).replaceActivity(MainActivity::class.java)
            }
            else{
                showToast(it.exception?.message.toString())
            }
        }
    }

    companion object {
        private const val ID = "ID"
        private const val PHONE_NUMBER = "PHONE_NUMBER"

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