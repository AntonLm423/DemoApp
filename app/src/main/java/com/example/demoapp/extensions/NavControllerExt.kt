package com.example.demoapp.extensions

import android.net.Uri
import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator

fun NavController.safeNavigate(@IdRes resId: Int) {
    handleUnsafeNavigating {
        navigate(resId)
    }
}

fun NavController.safeNavigate(@IdRes resId: Int, args: Bundle?) {
    handleUnsafeNavigating {
        navigate(resId, args)
    }
}

fun NavController.safeNavigate(@IdRes resId: Int, args: Bundle?, navOptions: NavOptions?) {
    handleUnsafeNavigating {
        navigate(resId, args, navOptions)
    }
}

fun NavController.safeNavigate(
    @IdRes resId: Int,
    args: Bundle?,
    navOptions: NavOptions?,
    navigatorExtras: Navigator.Extras?
) {
    handleUnsafeNavigating {
        navigate(resId, args, navOptions, navigatorExtras)
    }
}

fun NavController.safeNavigate(deepLink: Uri) {
    handleUnsafeNavigating {
        navigate(deepLink)
    }
}

fun NavController.safeNavigate(deepLink: Uri, navOptions: NavOptions?) {
    handleUnsafeNavigating {
        navigate(deepLink, navOptions)
    }
}

fun NavController.safeNavigate(
    deepLink: Uri,
    navOptions: NavOptions?,
    navigatorExtras: Navigator.Extras?
) {
    handleUnsafeNavigating {
        navigate(deepLink, navOptions, navigatorExtras)
    }
}

fun NavController.safeNavigate(request: NavDeepLinkRequest) {
    handleUnsafeNavigating {
        navigate(request)
    }
}

fun NavController.safeNavigate(request: NavDeepLinkRequest, navOptions: NavOptions?) {
    handleUnsafeNavigating {
        navigate(request, navOptions)
    }
}

fun NavController.safeNavigate(
    request: NavDeepLinkRequest,
    navOptions: NavOptions?,
    navigatorExtras: Navigator.Extras?
) {
    handleUnsafeNavigating {
        navigate(request, navOptions, navigatorExtras)
    }
}

fun NavController.safeNavigate(directions: NavDirections) {
    handleUnsafeNavigating {
        navigate(directions)
    }
}

fun NavController.safeNavigate(directions: NavDirections, navOptions: NavOptions?) {
    handleUnsafeNavigating {
        navigate(directions, navOptions)
    }
}

fun NavController.safeNavigate(directions: NavDirections, navigatorExtras: Navigator.Extras) {
    handleUnsafeNavigating {
        navigate(directions, navigatorExtras)
    }
}

fun NavController.safeNavigate(route: String, builder: NavOptionsBuilder.() -> Unit) {
    handleUnsafeNavigating {
        navigate(route, builder)
    }
}

private fun handleUnsafeNavigating(block: () -> Unit) {
    try {
        block()
    } catch (t: Throwable) {
        // Nothing to do
    }
}
