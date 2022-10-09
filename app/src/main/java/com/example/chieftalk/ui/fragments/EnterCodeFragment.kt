package com.example.chieftalk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chieftalk.databinding.FragmentEnterCodeBinding
import com.example.chieftalk.utilits.AppTextWatcher
import com.example.chieftalk.utilits.showToast


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
        binding.registerInputCode.addTextChangedListener(AppTextWatcher{
            if (it.toString().length >= 6){
                verifyCode()
            }
        })
    }

    private fun verifyCode() {
        showToast("Окей")
    }

    companion object {
        @JvmStatic
        fun newInstance() = EnterCodeFragment()
    }
}