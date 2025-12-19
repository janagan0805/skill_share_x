package com.example.skillsharex

import androidx.test.platform.app.InstrumentationRegistry
import com.example.skillsharex.data.UserRepository
import org.junit.Assert.assertNotNull
import org.junit.Test

class AdditionalUsersInstrumentedTest {
    @Test
    fun canLoginWithExtraUsersJson() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val repo = UserRepository(context)
        assertNotNull(repo.findUser("charlie", "charlie123"))
        assertNotNull(repo.findUser("diana@skillsharex.com", "diana456"))
    }
}
