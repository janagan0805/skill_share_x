package com.example.skillsharex.navigation

import androidx.navigation.NavController

fun NavController.safeNavigate(route: String) {
    if (this.currentDestination?.route != route) {
        try {
            this.navigate(route)
        } catch (_: Exception) {}
    }
}
