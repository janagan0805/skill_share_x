package com.example.skillsharex.data.session

import android.content.Context
import android.content.SharedPreferences

class UserSession(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun saveUser(id: String, name: String, email: String) {
        with(sharedPreferences.edit()) {
            putString("user_id", id)
            putString("user_name", name)
            putString("user_email", email)
            putBoolean("is_logged_in", true)
            apply()
        }
    }

    fun getUserId(): String? = sharedPreferences.getString("user_id", null)
    fun getUserName(): String? = sharedPreferences.getString("user_name", null)
    fun getUserEmail(): String? = sharedPreferences.getString("user_email", null)
    fun isLoggedIn(): Boolean = sharedPreferences.getBoolean("is_logged_in", false)

    fun clearSession() {
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }
    }
}