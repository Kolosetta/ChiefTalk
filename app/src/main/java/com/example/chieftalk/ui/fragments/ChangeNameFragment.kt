package com.example.chieftalk.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.chieftalk.MainActivity
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentChangeNameBinding
import com.example.chieftalk.utilits.*

class ChangeNameFragment : Fragment() {

    private lateinit var binding: FragmentChangeNameBinding
    private lateinit var mainActivity: MainActivity
    private var name: String? = null
    private var surname: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

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
        setHasOptionsMenu(true)
    }

    override fun onStart() {
        super.onStart()
        mainActivity.appDrawer.disableDrawer()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        mainActivity.menuInflater.inflate(R.menu.settings_menu_confirm, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings_confirm_change -> changeName()
        }
        return true
    }

    private fun changeName() {
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