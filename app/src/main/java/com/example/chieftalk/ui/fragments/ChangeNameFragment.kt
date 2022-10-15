package com.example.chieftalk.ui.fragments

import android.os.Bundle
import android.view.*
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentChangeNameBinding
import com.example.chieftalk.utilits.*

class ChangeNameFragment : BaseChangeFragment() {

    private lateinit var binding: FragmentChangeNameBinding
    private var name: String? = null
    private var surname: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         arguments?.let {
             name = it.getString(NAME)
             surname = it.getString(SURNAME)
       }
    }

    override fun onCreateView(inflater: LayoutInflater, cont: ViewGroup?, bundle: Bundle?): View {
        binding = FragmentChangeNameBinding.inflate(inflater, cont, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.settingsInputName.setText(name)
        binding.settingsInputSurname.setText(surname)
    }

    override fun change() {
        val name = binding.settingsInputName.text.toString()
        val surname = binding.settingsInputSurname.text.toString()
        if (name.isEmpty()) {
            showToast(getString(R.string.settings_toast_name_is_empty))
        } else {
            val fullname = "$name $surname".trim()
            REF_DATABASE_ROOT.child(NODE_USERS).child(UID).child(CHILD_FULLNAME)
                .setValue(fullname)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        showToast(getString(R.string.toast_data_updated))
                        USER.fullName = fullname
                        mainActivity.appDrawer.updateHeader()
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
        }
    }

    companion object {
        private const val NAME = "name"
        private const val SURNAME = "surname"

        @JvmStatic
        fun newInstance(name: String, surname: String) =
            ChangeNameFragment().apply {
                arguments = Bundle().apply {
                    putString(NAME, name)
                    putString(SURNAME, surname)
                }
            }
    }
}