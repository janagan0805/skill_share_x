package com.example.skillsharex.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.outlined.Book
import androidx.compose.ui.graphics.vector.ImageVector

object Routes {

    // Root graph
    const val ROOT_GRAPH = "root_graph"
    const val SPLASH = "splash"
    const val ONBOARDING = "onboarding"

    // Auth graph
    const val AUTH_GRAPH = "auth_graph"
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val FORGOT_PASSWORD = "forgot_password"

    // Main graph
    const val MAIN_GRAPH = "main_graph"

    // Bottom tabs
    const val HOME = "home"
    const val COMMUNITY = "community"
    const val SESSIONS = "sessions"
    const val MENTORS = "mentors"
    const val PROFILE = "profile"

    // Other screens
    const val NOTIFICATIONS = "notifications"
    const val SETTINGS = "settings"
    const val EDIT_PROFILE = "edit_profile"
    const val MY_COURSES = "user_course"
    const val CREATE_COURSE = "create_course"
    const val CREATE_POST = "create_post"
    const val MENTORSHIP_REQUESTS = "mentorship_requests"
    const val CHATBOT = "chatbot"

    // Dynamic
    const val COURSE_DETAIL = "courseDetail/{courseId}"
    const val EDIT_COURSE = "edit_course/{courseId}"
    const val MENTOR_DETAIL = "mentorDetail/{mentorId}"
    const val POST_DETAIL = "post_detail/{postId}"

    // Sessions
    const val SESSION_OVERVIEW = "session_overview/{sessionId}"
    const val LIVE_SESSION = "live_session/{sessionId}"

}

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Home : BottomNavItem(Routes.HOME, Icons.Default.Home, "Home")
    object Community : BottomNavItem(Routes.COMMUNITY, Icons.Default.People, "Commu")
    object Sessions : BottomNavItem(Routes.SESSIONS, Icons.Outlined.Book, "Sessions")
    object Mentors : BottomNavItem(Routes.MENTORS, Icons.Default.People, "Mentors")
    object Profile : BottomNavItem(Routes.PROFILE, Icons.Default.AccountCircle, "Profile")
}
