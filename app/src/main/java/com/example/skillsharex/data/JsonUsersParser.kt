package com.example.skillsharex.data

import org.json.JSONArray

object JsonUsersParser {
    fun findUser(json: String, username: String, password: String): User? {
        val arr = JSONArray(json)
        for (i in 0 until arr.length()) {
            val obj = arr.getJSONObject(i)
            val u = obj.getString("username")
            val p = obj.getString("password")
            val email = obj.optString("email")
            if ((u == username || email == username) && p == password) {
                return User(
                    username = u,
                    password = p,
                    fullName = obj.optString("fullName"),
                    email = obj.optString("email")
                )
            }
        }
        return null
    }
}
