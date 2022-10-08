package com.example.chieftalk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentEnterPhoneBinding


class EnterPhoneFragment : Fragment() {

    private lateinit var binding: FragmentEnterPhoneBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEnterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        binding.registerBtnNext.setOnClickListener {
            sendCode()
        }
    }

    private fun sendCode() {
        if(binding.registerInputPhone.text.toString().isEmpty()){
            Toast.makeText(requireContext(),
                getString(R.string.register_toast_enter_phone),
                Toast.LENGTH_SHORT).show()
        }
        else {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.registerContainer, EnterCodeFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = EnterPhoneFragment()
    }
}