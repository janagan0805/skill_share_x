package com.example.skillsharex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.skillsharex.ui.chatbot.ChatbotScreen
import com.example.skillsharex.ui.community.*
import com.example.skillsharex.ui.course.CourseDetailScreen
import com.example.skillsharex.ui.forgot.ForgotPasswordScreen
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.login.LoginScreen
import com.example.skillsharex.ui.mentor.*
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.profile.CreateCourseScreen
import com.example.skillsharex.ui.profile.EditCourseDetailScreen
import com.example.skillsharex.ui.profile.EditProfileScreen
import com.example.skillsharex.ui.profile.ProfileScreen
import com.example.skillsharex.ui.profile.UserCourseScreen
import com.example.skillsharex.ui.requests.MentorshipRequestsScreen
import com.example.skillsharex.ui.session.*
import com.example.skillsharex.ui.sessions.SessionDetailScreen
import com.example.skillsharex.ui.sessions.SessionScreen
import com.example.skillsharex.ui.settings.SettingsScreen
import com.example.skillsharex.ui.signup.SignUpScreen
import com.example.skillsharex.ui.splash.AppSplashScreen
import com.example.skillsharex.ui.splash.OnboardingScreen
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.viewmodel.*
import com.example.skillsharex.viewmodel.community.CommunityViewModel
import com.example.skillsharex.viewmodel.community.CreatePostViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    /* ---------- VIEWMODELS (SINGLE SOURCE OF TRUTH) ---------- */
    val profileViewModel: ProfileViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()
    val createPostViewModel: CreatePostViewModel = viewModel()
    val sessionViewModel: SessionViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        /* -------- SPLASH & ONBOARDING -------- */
        composable("splash") {
            AppSplashScreen {
                when {
                    sessionManager.isFirstLaunch() ->
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }

                    sessionManager.isLoggedIn() ->
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }

                    else ->
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                }
            }
        }

        composable("onboarding") {
            OnboardingScreen(
                onFinish = {
                    sessionManager.setFirstLaunchDone()
                    navController.navigate("login") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                }
            )
        }

        /* -------- AUTH -------- */
        composable("login") {
            LoginScreen(
                onLoginSuccess = { userName ->
                    sessionManager.saveUserName(userName)
                    sessionManager.setLoggedIn(true)
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = { navController.navigate("signup") },
                onForgotPasswordClick = { navController.navigate("forgotPassword") }
            )
        }

        composable("signup") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("login") {
                        popUpTo("signup") { inclusive = true }
                    }
                },
                onBackToLogin = { navController.popBackStack() }
            )
        }

        composable("forgotPassword") {
            ForgotPasswordScreen(onBackToLogin = { navController.popBackStack() })
        }

        /* -------- MAIN -------- */
        composable("home") { HomeDashboardScreen(navController) }
        composable("mentors") { MentorListScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }

        /* -------- CHATBOT -------- */
        composable("session_list") {
            SessionListScreen(navController)
        }

        composable("chatbot") {
            ChatbotScreen(navController)
        }

        /* -------- COMMUNITY -------- */
        composable("community") {
            CommunityScreen(
                navController = navController,
                viewModel = communityViewModel
            )
        }

        composable("create_post") {
            CreatePostScreen(
                navController = navController,
            )
        }

        composable(
            route = "post_detail/{postId}",
            arguments = listOf(navArgument("postId") { type = NavType.StringType })
        ) { backStackEntry ->
            PostDetailScreen(
                navController = navController,
                postId = backStackEntry.arguments?.getString("postId") ?: ""
            )
        }

        /* -------- PROFILE -------- */
        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }

        composable(Screen.UserCourse.route) {
            UserCourseScreen(navController)
        }

        composable(
            route = "edit_course/{courseId}",
            arguments = listOf(navArgument("courseId") { type = NavType.IntType })
        ) {
            EditCourseDetailScreen(navController = navController)
        }


        composable("create_course") {
            CreateCourseScreen(navController)
        }


        composable("settings") {
            SettingsScreen(navController)
        }

        /* -------- REQUESTS -------- */
        composable(Screen.Requests.route) {
            MentorshipRequestsScreen(navController)
        }

        /* -------- SESSIONS (FINAL FLOW) -------- */

        composable("sessions") {
            SessionScreen(navController)
        }

        composable("sessionDetail/{sessionId}") { backStackEntry ->
            SessionDetailScreen(
                navController = navController,
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            )
        }

        composable(
            route = "session_overview/{sessionId}",
            arguments = listOf(
                navArgument("sessionId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val sessionId = backStackEntry.arguments?.getInt("sessionId")

            // Safety check
            if (sessionId == null) {
                // You can pop back or show error UI
                navController.popBackStack()
                return@composable
            }

            SessionOverviewScreen(
                navController = navController,
                sessionId = sessionId
            )
        }


        composable("live_session/{sessionId}") { backStackEntry ->
            LiveSessionScreen(
                navController = navController,
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            )
        }

        /* -------- DETAILS -------- */
        composable(
            "mentorDetail/{mentorId}",
            arguments = listOf(navArgument("mentorId") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.getInt("mentorId")?.let {
                MentorDetailScreen(navController, it)
            }
        }

        composable("courseDetail/{courseId}",
            listOf(navArgument("courseId") { type = NavType.IntType })) {
                CourseDetailScreen(navController = navController)
        }

        composable(Screen.SkillFilter.route) {
            SkillFilterScreen(navController)
        }
    }
}

