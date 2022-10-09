package com.example.chieftalk.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.chieftalk.MainActivity
import com.example.chieftalk.R
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.FragmentSettingsBinding
import com.example.chieftalk.utilits.AUTH
import com.example.chieftalk.utilits.replaceActivity


class SettingsFragment : Fragment() {
    //TODO добавить анимации на кнопки редаткирования
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        requireActivity().menuInflater.inflate(R.menu.setting_action_menu, menu)
    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                (requireActivity() as MainActivity).replaceActivity(RegisterActivity::class.java)
            }
        }
        return true
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}