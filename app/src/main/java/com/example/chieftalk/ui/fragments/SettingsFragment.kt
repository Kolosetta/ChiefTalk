package com.example.chieftalk.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.chieftalk.MainActivity
import com.example.chieftalk.R
import com.example.chieftalk.activities.RegisterActivity
import com.example.chieftalk.databinding.FragmentSettingsBinding
import com.example.chieftalk.utilits.*
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView


class SettingsFragment : Fragment() {
    //TODO добавить анимации на кнопки редаткирования
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onCreateView(inflater: LayoutInflater, cont: ViewGroup?, bundle: Bundle?): View {
        binding = FragmentSettingsBinding.inflate(inflater, cont, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mainActivity.appDrawer.disableDrawer()
        initFields()
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
        when (item.itemId) {
            R.id.settings_menu_exit -> {
                AUTH.signOut()
                mainActivity.replaceActivity(RegisterActivity::class.java)
            }
            R.id.settings_menu_edit -> {
                mainActivity.supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.main_container, ChangeNameFragment.newInstance(
                            USER.fullName.substringBefore(" "),
                            USER.fullName.substringAfter(" ")
                        )
                    )
                    .addToBackStack(null)
                    .commit()
            }
        }
        return true
    }

    private fun initFields() {
        binding.settingsBio.text = USER.bio
        binding.settingsFullName.text = USER.fullName
        binding.settingsPhoneNumber.text = USER.phone
        binding.settingsStatus.text = USER.status
        binding.settingsUsername.text = USER.userName
        binding.settingsBtnChangeUsername.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, ChangeUserNameFragment.newInstance(USER.userName))
                .addToBackStack(null)
                .commit()
        }
        binding.settingsBtnChangeBio.setOnClickListener {
            mainActivity.supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, ChangeBioFragment.newInstance(USER.bio))
                .addToBackStack(null)
                .commit()
        }
        binding.settingsChangeAvatar.setOnClickListener {
            startChangeUserAvatarActivity()
        }
    }

    private fun startChangeUserAvatarActivity() {
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(600, 600)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(mainActivity)
    }


    companion object {
        fun newInstance() = SettingsFragment()
    }
}