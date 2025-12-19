package com.example.skillsharex.data

import android.content.Context
import android.content.res.Resources
import com.example.skillsharex.R

class UserRepository(private val context: Context) {
    fun findUser(username: String, password: String): User? {
        val res = context.resources
        val primary = res.openRawResource(R.raw.users).bufferedReader().use { it.readText() }
        JsonUsersParser.findUser(primary, username, password)?.let { return it }
        return try {
            val extra = res.openRawResource(R.raw.extra_users).bufferedReader().use { it.readText() }
            JsonUsersParser.findUser(extra, username, password)
        } catch (_: Resources.NotFoundException) {
            null
        }
    }
}
