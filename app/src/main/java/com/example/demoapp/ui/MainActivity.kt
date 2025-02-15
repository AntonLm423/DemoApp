package com.example.demoapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.fragment.NavHostFragment
import com.example.demoapp.R
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.extensions.addSystemWindowInsetToPadding
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null
    private val binding get() = viewBinding as ActivityMainBinding

    /** добавлять ли инсеты, при false - контент под системными окнами (edge-to-edge) */
    var topInset = true
    var leftInset = true
    var rightInset = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) 
        enableEdgeToEdge()
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        processInsets()
        initNavigation()
    }

    override fun onDestroy() {
        viewBinding = null
        super.onDestroy()
    }

    private fun initNavigation() = with(binding.bottomNavigationView) {
        setOnItemReselectedListener(onItemReselectedListener)
        addSystemWindowInsetToPadding(bottom = true)
    }

    /**
     * Navigation
     */
    private val navController
        get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    private val onItemReselectedListener = NavigationBarView.OnItemReselectedListener { item ->
        when (item.itemId) {
            R.id.action_main -> {
                navController.popBackStack(R.id.mainFragment, false)
            }

            R.id.action_catalog -> {
                // TODO:
            }

            R.id.action_favorites -> {
                // TODO:
            }

            R.id.action_more -> {
                // TODO:
            }
        }
    }

    private fun processInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insetsCompat ->
            val insets = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(
                left = (if (leftInset) insets.left else 0),
                top = (if (topInset) insets.top else 0),
                right = (if (rightInset) insets.right else 0),
            )

            insetsCompat
        }
    }

    fun setBottomNavigationViewVisible(isVisible: Boolean) {
        binding.bottomNavigationView.isVisible = isVisible
    }
}