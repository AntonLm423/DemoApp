package com.example.demoapp.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentMainBinding
import com.example.demoapp.ui.base.BaseFragment

class MainFragment : BaseFragment() {

    override val destinationId = R.id.mainFragment

    private val binding get() = viewBinding as FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}
