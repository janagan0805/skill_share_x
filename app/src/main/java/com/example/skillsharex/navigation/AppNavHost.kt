package com.example.skillsharex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.skillsharex.ui.*
import com.example.skillsharex.ui.call.CallScreen
import com.example.skillsharex.ui.chat.ChatScreen
import com.example.skillsharex.ui.community.*
import com.example.skillsharex.ui.forgot.ForgotPasswordScreen
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.login.LoginScreen
import com.example.skillsharex.ui.mentor.*
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.profile.EditProfileScreen
import com.example.skillsharex.ui.profile.ProfileScreen
import com.example.skillsharex.ui.profile.ProfileViewModel
import com.example.skillsharex.ui.requests.MentorshipRequestsScreen
import com.example.skillsharex.ui.session.*
import com.example.skillsharex.ui.sessions.*
import com.example.skillsharex.ui.settings.SettingsScreen
import com.example.skillsharex.ui.signup.SignUpScreen
import com.example.skillsharex.ui.splash.AppSplashScreen
import com.example.skillsharex.ui.splash.OnboardingScreen
import com.example.skillsharex.utils.SessionManager

@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val session = SessionManager(context)

    // 🔥 SHARED PROFILE VIEWMODEL (FRONTEND STATE)
    val profileViewModel: ProfileViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {

        /* -------- SPLASH -------- */
        composable("splash") {
            AppSplashScreen {
                when {
                    session.isFirstLaunch() -> {
                        navController.navigate("onboarding") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    session.isLoggedIn() -> {
                        navController.navigate("home") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                    else -> {
                        navController.navigate("login") {
                            popUpTo("splash") { inclusive = true }
                        }
                    }
                }
            }
        }

        /* -------- ONBOARDING -------- */
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

        /* -------- LOGIN -------- */
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
            ForgotPasswordScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }

        /* -------- MAIN -------- */
        composable("home") { HomeDashboardScreen(navController) }
        composable("mentors") { MentorListScreen(navController) }
        composable("community") { CommunityScreen(navController) }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }

        composable("settings") { SettingsScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }

        /* -------- EDIT PROFILE (FIXED) -------- */
        composable(Screen.EditProfile.route) {
            EditProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }

        /* -------- DETAILS -------- */
        composable(
            "mentorDetail/{mentorId}",
            arguments = listOf(navArgument("mentorId") { type = NavType.StringType })
        ) {
            MentorDetailScreen(navController)
        }

        composable(Screen.Requests.route) {
            MentorshipRequestsScreen(navController)
        }

        composable(
            "chat/{mentorId}",
            arguments = listOf(navArgument("mentorId") { type = NavType.StringType })
        ) {
            ChatScreen(navController)
        }

        composable("call") {
            CallScreen(navController)
        }

        composable("sessions") {
            SessionScreen(navController)
        }

        composable(
            "sessionDetail/{sessionId}"
        ) { backStackEntry ->
            SessionDetailScreen(
                navController = navController,
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            )
        }

        composable(Screen.SessionList.route) {
            SessionListScreen(navController)
        }

        composable(
            route = "session_overview/{sessionId}",
            arguments = listOf(navArgument("sessionId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            SessionOverviewScreen(
                navController = navController,
                sessionId = backStackEntry.arguments?.getString("sessionId") ?: ""
            )
        }

        composable(
            route = "live_session/{sessionId}",
            arguments = listOf(navArgument("sessionId") {
                type = NavType.StringType
            })
        ) {
            LiveSessionScreen(
                navController = navController,
                sessionId = it.arguments?.getString("sessionId") ?: ""
            )
        }

        composable("createPost") { CreatePostScreen(navController) }

        composable(
            route = "post_detail/{postTitle}",
            arguments = listOf(navArgument("postTitle") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            PostDetailScreen(
                navController = navController,
                postTitle = backStackEntry.arguments?.getString("postTitle") ?: ""
            )
        }

        composable(Screen.SkillFilter.route) {
            SkillFilterScreen(navController)
        }
    }
}
