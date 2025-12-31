package com.example.skillsharex.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("skillsharex_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LOGGED_IN = "is_logged_in"
        private const val KEY_FIRST_LAUNCH = "is_first_launch"
        private const val KEY_USER_NAME = "user_name"
    }

    /* ---------- AUTH ---------- */

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_LOGGED_IN, false)
    }

    fun setLoggedIn(value: Boolean) {
        prefs.edit().putBoolean(KEY_LOGGED_IN, value).apply()
    }

    fun logout() {
        prefs.edit().clear().apply()
    }

    /* ---------- USER ---------- */

    fun saveUserName(name: String) {
        prefs.edit().putString(KEY_USER_NAME, name).apply()
    }

    fun getUserName(): String {
        return prefs.getString(KEY_USER_NAME, "User") ?: "User"
    }

    /* ---------- ONBOARDING ---------- */

    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean(KEY_FIRST_LAUNCH, true)
    }

    fun setFirstLaunchDone() {
        prefs.edit().putBoolean(KEY_FIRST_LAUNCH, false).apply()
    }
    fun saveProfileImageUri(uri: String) {
        prefs.edit().putString("profile_image_uri", uri).apply()
    }

    fun getProfileImageUri(): String? {
        return prefs.getString("profile_image_uri", null)
    }
    fun saveUserId(userId: Int) {
        prefs.edit().putInt("user_id", userId).apply()
    }
    fun getUserId(): Int {
        return prefs.getInt("user_id", 0)
    }

    fun saveProfileImageUrl(url: String) {
        prefs.edit().putString("profile_image_url", url).apply()
    }

    fun getProfileImageUrl(): String? {
        return prefs.getString("profile_image_url", null)
    }


}
