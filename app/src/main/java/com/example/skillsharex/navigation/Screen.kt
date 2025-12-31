package com.example.skillsharex.navigation

sealed class Screen(val route: String) {

    /* -------- STATIC SCREENS -------- */

    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")

    object Login : Screen("login")
    object SignUp : Screen("signup")

    object Home : Screen("home")

    object Community : Screen("community")
    object CreatePost : Screen("create_post")
    object SkillFilter : Screen("skill_filter")

    object Courses : Screen("courses")
    object Mentors : Screen("mentors")

    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")

    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
    object Requests : Screen("requests")
    object SessionList : Screen("session_list")
    object SessionOverview : Screen("session_overview")


    /* -------- DYNAMIC SCREENS (IMPORTANT) -------- */

    object Overview : Screen("overview/{courseId}") {
        fun createRoute(courseId: String): String {
            return "overview/$courseId"
        }
    }

    object CourseDetail : Screen("course_detail/{courseId}") {
        fun createRoute(courseId: String): String {
            return "course_detail/$courseId"
        }
    }

    object MentorDetail : Screen("mentor_detail/{mentorId}") {
        fun createRoute(mentorId: String): String {
            return "mentor_detail/$mentorId"
        }
    }

    object PostDetail : Screen("post_detail/{title}") {
        fun createRoute(title: String): String {
            return "post_detail/$title"
        }
    }

    object Chat : Screen("chat/{userId}") {
        fun createRoute(userId: String): String {
            return "chat/$userId"
        }
    }
}
