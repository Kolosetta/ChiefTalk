package com.example.chieftalk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chieftalk.MainActivity
import com.example.chieftalk.R
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.FragmentEnterPhoneBinding
import com.example.chieftalk.utilits.AUTH
import com.example.chieftalk.utilits.replaceActivity
import com.example.chieftalk.utilits.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class EnterPhoneFragment : Fragment() {

    private lateinit var binding: FragmentEnterPhoneBinding
    private lateinit var phoneNumber: String
    private lateinit var verifyCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        verifyCallBack = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
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

            override fun onVerificationFailed(exceptio: FirebaseException) {
                showToast(exceptio.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                super.onCodeSent(id, token)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.registerContainer, EnterCodeFragment.newInstance(id, phoneNumber))
                    .addToBackStack(null)
                    .commit()
            }
        }
        binding.registerBtnNext.setOnClickListener { sendCode() }
    }

    private fun sendCode() {
        if(binding.registerInputPhone.text.toString().isEmpty()){
            showToast(getString(R.string.register_toast_enter_phone))
        }
        else {
            authUser()
        }
    }

    private fun authUser() {
        phoneNumber = binding.registerInputPhone.text.toString()
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity() as RegisterActivity,
            verifyCallBack
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() = EnterPhoneFragment()
    }
}