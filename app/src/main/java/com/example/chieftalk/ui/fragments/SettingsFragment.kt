package com.example.chieftalk.ui.fragments

import android.app.Activity
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
    private lateinit var mainActivity: MainActivity

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if(activity is MainActivity){
            mainActivity = activity
        }
    }

    override fun onCreateView(inflater: LayoutInflater, cont: ViewGroup?, bundle: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, cont, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mainActivity.appDrawer.disableDrawer()

    }

    override fun onStop() {
        super.onStop()
        mainActivity.appDrawer.enableDrawer()

    }

    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        mainActivity.menuInflater.inflate(R.menu.setting_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                mainActivity.replaceActivity(RegisterActivity::class.java)
            }
        }
        return true
    }

    companion object {
        fun newInstance() = SettingsFragment()
    }
}