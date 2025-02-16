package com.example.demoapp.ui.catalog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.demoapp.R
import com.example.demoapp.databinding.FragmentCatalogBinding
import com.example.demoapp.ui.base.BaseFragment

class CatalogFragment() : BaseFragment() {

    override val destinationId = R.id.catalogFragment

    private val binding get() = viewBinding as FragmentCatalogBinding

    private val catalogViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CatalogViewModel::class]
    }

    override fun initOperations(savedInstanceState: Bundle?) {
        catalogViewModel.searchMovies("Человек-паук")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        viewBinding = FragmentCatalogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onSetupLayout(savedInstanceState: Bundle?) {

    }

    override fun onSubscribeViewModel() {

    }
}
