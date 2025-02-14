package com.example.demoapp.ui.base

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.IdRes
import androidx.browser.customtabs.CustomTabColorSchemeParams
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.airbnb.lottie.LottieAnimationView
import com.example.demoapp.R
import com.example.demoapp.data.local.prefs.PreferenceStorage
import com.example.demoapp.extensions.addSystemWindowInsetToMargin
import com.example.demoapp.extensions.dpToPx
import com.example.demoapp.extensions.getColorCompat
import com.example.demoapp.ui.MainActivity
import javax.inject.Inject

abstract class BaseFragment : Fragment() {

    companion object {
        private const val BOTTOM_NAVIGATION_VIEW_HEIGHT = 56
    }

    @Inject
    lateinit var preferenceStorage: PreferenceStorage

    protected var connectivityManager: ConnectivityManager? = null

    protected var viewBinding: ViewBinding? = null

    open val showBottomNavigationView: Boolean = false

    abstract val destinationId: Int?

    /** добавлять ли вставки, при false - контент под системными окнами (edge-to-edge) */
    open val topInset = true
    open val bottomInset = true
    open val leftInset = true
    open val rightInset = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArguments()
        initOperations(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as? MainActivity)?.apply {
            topInset = this@BaseFragment.topInset
            leftInset = this@BaseFragment.leftInset
            rightInset = this@BaseFragment.rightInset
        }
        onSetupLayout(savedInstanceState)
        onSubscribeViewModel()

        processInsets()
    }

    override fun onStop() {
        super.onStop()
        (requireActivity() as? MainActivity)?.apply {
            topInset = true
            leftInset = true
            rightInset = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    private fun processInsets() {
        viewBinding?.root?.updatePaddingOnKeyboardVisibilityChanged()
        if (bottomInset) {
            viewBinding?.root?.addSystemWindowInsetToMargin(bottom = true)
        }
    }

    protected fun <T : View> T.postSafe(postCallback: (T) -> Unit) {
        this.post {
            if (this@BaseFragment.isAdded) {
                postCallback.invoke(this)
            }
        }
    }

    protected fun setBottomNavigationViewVisible(isVisible: Boolean) {
        (requireActivity() as? MainActivity)?.setBottomNavigationViewVisible(isVisible)
    }

    /** Извлечение аргументов */
    protected open fun handleArguments() {}

    /** Вызывать методы вью модели, которые получают данные из репозиториев */
    protected open fun initOperations(savedInstanceState: Bundle?) {}

    /** Установка верстки фрагмента */
    protected open fun onSetupLayout(savedInstanceState: Bundle?) {}

    /** Подписки на State/LiveData */
    protected open fun onSubscribeViewModel() {}

    /**
     * Keyboard listener
     */
    var onUpdatePaddingAction: ((Boolean) -> Unit?)? = null

    fun View.updatePaddingOnKeyboardVisibilityChanged() {
        addKeyboardInsetListener(this) { isOpen: Boolean, height: Int ->
            runCatching {
                setBottomNavigationViewVisible(!isOpen)
                onUpdatePaddingAction?.invoke(isOpen)

                val bottomNavHeight = BOTTOM_NAVIGATION_VIEW_HEIGHT.dpToPx()

                view?.updatePadding(
                    bottom = if(isOpen) {
                        height + (view?.paddingBottom ?: 0) - bottomNavHeight
                    } else {
                        bottomNavHeight
                    }
                )
            }
        }
    }

    fun addKeyboardInsetListener(rootView: View, keyboardCallback: (visible: Boolean, Int) -> Unit) {
        rootView.postSafe {
            var isKeyboardVisible = false

            ViewCompat.setOnApplyWindowInsetsListener(it) { view, insetsCompat ->
                val keyboardHeight = insetsCompat.getInsets(WindowInsetsCompat.Type.ime()).bottom
                val systemBarsHeight = insetsCompat.getInsets(WindowInsetsCompat.Type.systemBars()).bottom

                val keyboardUpdateCheck = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    keyboardHeight > 0
                } else {
                    val keyboardRatio = 0.25
                    val systemScreenHeight = requireActivity().window.decorView.height
                    val heightDiff = insetsCompat.systemWindowInsetBottom + insetsCompat.systemWindowInsetTop
                    heightDiff > keyboardRatio * systemScreenHeight
                }

                if (keyboardUpdateCheck != isKeyboardVisible) {
                    keyboardCallback(keyboardUpdateCheck, keyboardHeight - systemBarsHeight)
                    isKeyboardVisible = keyboardUpdateCheck
                }

                insetsCompat
            }
        }
    }
}