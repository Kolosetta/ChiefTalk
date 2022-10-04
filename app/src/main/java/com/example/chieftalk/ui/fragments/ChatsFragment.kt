package com.example.chieftalk.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chieftalk.databinding.FragmentChatsBinding


class ChatsFragment : Fragment() {

    private lateinit var binding: FragmentChatsBinding
    //22^40 часть 6
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatsBinding.inflate(inflater, container, false)
        return binding.root
    }
}