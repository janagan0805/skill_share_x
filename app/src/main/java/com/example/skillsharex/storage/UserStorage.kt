package com.example.skillsharex.storage

import android.content.Context
import com.example.skillsharex.data.User
import org.json.JSONObject
import java.io.File

class UserStorage(private val context: Context) {
    private val fileName = "current_user.json"

    fun save(user: User) {
        val obj = JSONObject()
        obj.put("username", user.username)
        obj.put("fullName", user.fullName)
        obj.put("email", user.email)
        val file = File(context.filesDir, fileName)
        file.writeText(obj.toString())
    }

    fun exists(): Boolean {
        val file = File(context.filesDir, fileName)
        return file.exists()
    }

    fun loadName(): String? {
        val file = File(context.filesDir, fileName)
        if (!file.exists()) return null
        val obj = JSONObject(file.readText())
        val full = obj.optString("fullName")
        if (full.isNotEmpty()) return full
        val username = obj.optString("username")
        return if (username.isNotEmpty()) username else null
    }
}
