package com.example.chieftalk.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.chieftalk.R
import com.example.chieftalk.databinding.FragmentSettingsBinding


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

    companion object {
        fun newInstance() = SettingsFragment()
    }
}