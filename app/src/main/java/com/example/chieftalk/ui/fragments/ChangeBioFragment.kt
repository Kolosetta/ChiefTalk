package com.example.chieftalk.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentChangeBioBinding
import com.example.chieftalk.utilits.*

class ChangeBioFragment : BaseChangeFragment() {

    private lateinit var binding: FragmentChangeBioBinding
    private var bio: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            bio = it.getString(BIO)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, cont: ViewGroup?, bundle: Bundle?): View {
        binding = FragmentChangeBioBinding.inflate(inflater, cont, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsInputBio.setText(bio)
    }

    override fun change() {
        super.change()
        val newBio = binding.settingsInputBio.text.toString().trim()
        REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_BIO).setValue(newBio)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    showToast(getString(R.string.toast_data_updated))
                    USER.bio = newBio
                    mainActivity.supportFragmentManager.popBackStack()
                }
            }
    }

    companion object {
       private const val BIO = "bio"

        @JvmStatic
        fun newInstance(bio: String) =
            ChangeBioFragment().apply {
                arguments = Bundle().apply {
                    putString(BIO, bio)
                }
            }
    }
}