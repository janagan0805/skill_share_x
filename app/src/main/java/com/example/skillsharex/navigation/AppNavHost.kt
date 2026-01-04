package com.example.skillsharex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.skillsharex.ui.call.CallScreen
import com.example.skillsharex.ui.chat.ChatScreen
import com.example.skillsharex.ui.community.*
import com.example.skillsharex.ui.course.CourseDetailScreen
import com.example.skillsharex.ui.forgot.ForgotPasswordScreen
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.login.LoginScreen
import com.example.skillsharex.ui.mentor.*
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.profile.EditProfileScreen
import com.example.skillsharex.ui.profile.ProfileScreen
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

@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val session = SessionManager(context)

    /* ---------- SINGLE SOURCE OF TRUTH (VIEWMODELS) ---------- */
    val profileViewModel: ProfileViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()
    val createPostViewModel: CreatePostEventViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        /* -------- SPLASH & ONBOARDING -------- */
        composable("splash") {
            AppSplashScreen {
                when {
                    session.isFirstLaunch() ->
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }

                    session.isLoggedIn() ->
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
                    session.setFirstLaunchDone()
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
                    session.saveUserName(userName)
                    session.setLoggedIn(true)
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
                viewModel = createPostViewModel
            )
        }

        composable(
            route = "post_detail/{postTitle}",
            arguments = listOf(navArgument("postTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            PostDetailScreen(
                navController = navController,
                postTitle = backStackEntry.arguments?.getString("postTitle") ?: ""
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

        composable("settings") { SettingsScreen(navController) }

        /* -------- SESSIONS -------- */
        composable(Screen.Requests.route) {
            MentorshipRequestsScreen(navController)
        }

        composable(Screen.SessionList.route) {
            SessionListScreen(navController)
        }

        composable("sessions") {
            SessionScreen(navController)
        }

        composable("sessionDetail/{sessionId}") { backStackEntry ->
            SessionDetailScreen(
                navController = navController,
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            )
        }

        composable("session_overview/{sessionId}") { backStackEntry ->
            SessionOverviewScreen(
                navController = navController,
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
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

        composable("courseDetail/{courseId}") { backStackEntry ->
            backStackEntry.arguments?.getString("courseId")?.let {
                CourseDetailScreen(navController, it)
            }
        }

        composable("chat/{mentorId}") { ChatScreen(navController) }
        composable("call") { CallScreen(navController) }
        composable(Screen.SkillFilter.route) { SkillFilterScreen(navController) }
    }
}
