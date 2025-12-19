package com.example.skillsharex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.skillsharex.ui.chat.ChatScreen
import com.example.skillsharex.ui.community.CommunityScreen
import com.example.skillsharex.ui.community.CreatePostScreen
import com.example.skillsharex.ui.community.PostDetailScreen
import com.example.skillsharex.ui.community.SkillFilterScreen
import com.example.skillsharex.ui.courses.CourseCategoriesScreen
import com.example.skillsharex.ui.courses.CourseDetailScreen
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.login.LoginScreen
import com.example.skillsharex.ui.mentor.MentorDetailScreen
import com.example.skillsharex.ui.mentor.MentorListScreen
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.overview.MentorCourseOverviewScreen
import com.example.skillsharex.ui.profile.EditProfileScreen
import com.example.skillsharex.ui.profile.ProfileScreen
import com.example.skillsharex.ui.requests.MentorshipRequestsScreen
import com.example.skillsharex.ui.settings.SettingsScreen
import com.example.skillsharex.ui.signup.SignUpScreen
import com.example.skillsharex.ui.splash.SplashScreen
import com.example.skillsharex.ui.splash.OnboardingPager
import com.example.skillsharex.ui.theme.SkillShareXTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SkillShareXTheme {
                val navController = rememberNavController()
                SkillShareXNavHost(navController)
            }
        }
    }
}

/* ---------------- ROUTES ---------------- */

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")

    object Login : Screen("login")
    object SignUp : Screen("signup")

    object Home : Screen("home")
    object Community : Screen("community")
    object CreatePost : Screen("create_post")
    object PostDetail : Screen("post_detail/{title}")
    object SkillFilter : Screen("skill_filter")



    object Overview : Screen("overview/{courseId}")

    object Profile : Screen("profile")
    object EditProfile : Screen("edit_profile")

    object Courses : Screen("courses")
    object CourseDetail : Screen("course_detail/{courseId}")

    object Mentors : Screen("mentors")
    object MentorDetail : Screen("mentor_detail/{mentorId}")

    object Requests : Screen("requests")
    object Chat : Screen("chat/{userId}")

    object Notifications : Screen("notifications")
    object Settings : Screen("settings")
}

/* ---------------- NAV HOST ---------------- */

@Composable
fun SkillShareXNavHost(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {

        /* -------- SPLASH -------- */
        composable(Screen.Splash.route) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        /* -------- ONBOARDING -------- */
        composable(Screen.Onboarding.route) {
            OnboardingPager(
                onFinish = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                },
                onNavigateToAuth = { isSignIn ->
                    if (isSignIn) {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Onboarding.route) { inclusive = true }
                        }
                    } else {
                        navController.navigate(Screen.SignUp.route)
                    }
                }
            )
        }

        /* -------- LOGIN -------- */
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate(Screen.SignUp.route)
                },
                onForgotPasswordClick = {}
            )
        }

        /* -------- SIGN UP -------- */
        composable(Screen.SignUp.route) {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.SignUp.route) { inclusive = true }
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        /* -------- HOME (ROOT) -------- */
        composable(Screen.Home.route) {
            HomeDashboardScreen(
                onOpenProfile = { navController.navigate(Screen.Profile.route) },
                onOpenOverview = { id ->
                    navController.navigate("overview/$id")
                },
                onOpenCourses = { navController.navigate(Screen.Courses.route) },
                onOpenMentors = { navController.navigate(Screen.Mentors.route) },
                onOpenCommunity = { navController.navigate(Screen.Community.route) },
                onOpenNotifications = {
                    navController.navigate(Screen.Notifications.route)
                }
            )
        }

        /* -------- COMMUNITY -------- */
        composable(Screen.Community.route) {
            CommunityScreen(navController)
        }
        composable(Screen.CreatePost.route) {
            CreatePostScreen(navController)
        }
        composable("post_detail/{title}") { backStack ->
            PostDetailScreen(
                navController,
                backStack.arguments?.getString("title") ?: ""
            )
        }

        composable(Screen.SkillFilter.route) {
            SkillFilterScreen(navController)
        }



        /* -------- PROFILE -------- */
        composable(Screen.Profile.route) {
            ProfileScreen(
                navController = navController,
                onOpenSettings = {
                    navController.navigate(Screen.Settings.route)
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                }
            )
        }

        /* -------- EDIT PROFILE -------- */
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController)
        }

        /* -------- SETTINGS -------- */
        composable(Screen.Settings.route) {
            SettingsScreen(navController)
        }

        /* -------- COURSE OVERVIEW -------- */
        composable(Screen.Overview.route) { backStackEntry ->
            MentorCourseOverviewScreen(
                courseId = backStackEntry.arguments?.getString("courseId") ?: "",
                onBack = { navController.popBackStack() }
            )
        }

        /* -------- COURSES -------- */
        composable(Screen.Courses.route) {
            CourseCategoriesScreen(
                onBack = { navController.popBackStack() },
                onMenuClick = {},
                onCategoryClick = {
                    navController.navigate("course_detail/$it")
                }
            )
        }

        composable(Screen.CourseDetail.route) { backStackEntry ->
            CourseDetailScreen(
                backStackEntry.arguments?.getString("courseId") ?: ""
            )
        }

        /* -------- MENTORS -------- */
        composable(Screen.Mentors.route) {
            MentorListScreen(navController)
        }

        composable(Screen.MentorDetail.route) { backStackEntry ->
            MentorDetailScreen(
                navController,
                backStackEntry.arguments?.getString("mentorId") ?: ""
            )
        }

        /* -------- REQUESTS -------- */
        composable(Screen.Requests.route) {
            MentorshipRequestsScreen(navController)
        }

        /* -------- CHAT -------- */
        composable(Screen.Chat.route) { backStackEntry ->
            ChatScreen(
                navController = navController,
                userId = backStackEntry.arguments?.getString("userId") ?: ""
            )
        }

        /* -------- NOTIFICATIONS -------- */
        composable(Screen.Notifications.route) {
            NotificationsScreen(navController)
        }
    }
}
