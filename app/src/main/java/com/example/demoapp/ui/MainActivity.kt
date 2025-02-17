package com.example.demoapp.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.view.updatePadding
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.demoapp.R
import com.example.demoapp.databinding.ActivityMainBinding
import com.example.demoapp.extensions.safeNavigate
import com.google.android.material.navigation.NavigationBarView
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

    private var viewBinding: ActivityMainBinding? = null
    private val binding get() = viewBinding as ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        AndroidInjection.inject(this)
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

    /**
     * Navigation
     */
    private val navController
        get() = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

    private val onItemSelectedListener = NavigationBarView.OnItemSelectedListener { item ->
        val bundle = Bundle()
        val destination = when (item.itemId) {
            R.id.action_main -> {
                R.id.action_global_mainFragment
            }

            R.id.action_catalog -> {
                R.id.action_global_catalogFragment
            }

            R.id.action_favorites -> {
                0  // TODO
            }

            R.id.action_profile -> {
                0 // TODO
            }

            else -> {
                return@OnItemSelectedListener false
            }
        }

        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(false)
            .setRestoreState(true)
            .setPopUpTo(destinationId = navController.graph.startDestinationId, inclusive = false, saveState = true)

        navController.safeNavigate(destination, bundle, navOptions.build())

        true
    }

    private val onItemReselectedListener = NavigationBarView.OnItemReselectedListener { item ->
        when (item.itemId) {
            R.id.action_main -> {
                navController.popBackStack(R.id.mainFragment, false)
            }

            R.id.action_catalog -> {
                navController.popBackStack(R.id.catalogFragment, false)
            }

            R.id.action_favorites -> {
                // TODO:
            }

            R.id.action_catalog -> {
                // TODO:
            }
        }
    }

    private fun initNavigation() = with(binding.bottomNavigationView) {
        setOnItemSelectedListener(onItemSelectedListener)
        setOnItemReselectedListener(onItemReselectedListener)
    }

    fun setBottomNavigationViewVisible(isVisible: Boolean) {
        binding.bottomNavigationView.isVisible = isVisible
    }

    /**
     * Insets
     */

    /** добавлять ли инсеты, при false - контент под системными окнами (edge-to-edge) */
    var topInset = true
    var leftInset = true
    var rightInset = true


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
}
