package com.example.skillsharex

import com.example.skillsharex.data.JsonUsersParser
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class JsonUsersParserTest {
    private val json = """
        [
          {
            "username": "alice",
            "password": "password123",
            "fullName": "Alice Doe",
            "email": "alice@example.com"
          },
          {
            "username": "bob",
            "password": "qwerty456",
            "fullName": "Bob Smith",
            "email": "bob@example.com"
          }
        ]
    """.trimIndent()

    @Test
    fun findsValidUser() {
        val user = JsonUsersParser.findUser(json, "alice", "password123")
        assertNotNull(user)
    }

    @Test
    fun returnsNullForInvalidCredentials() {
        val user = JsonUsersParser.findUser(json, "alice", "wrong")
        assertNull(user)
    }
}
