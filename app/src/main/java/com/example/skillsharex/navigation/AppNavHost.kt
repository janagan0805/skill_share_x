package com.example.skillsharex.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.skillsharex.ui.*
import com.example.skillsharex.ui.chat.ChatScreen
import com.example.skillsharex.ui.community.CommunityScreen
import com.example.skillsharex.ui.community.CreatePostScreen
import com.example.skillsharex.ui.community.PostDetailScreen
import com.example.skillsharex.ui.community.SkillFilterScreen
import com.example.skillsharex.ui.forgot.ForgotPasswordScreen
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.mentor.MentorDetailScreen
import com.example.skillsharex.ui.mentor.MentorListScreen
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.profile.EditProfileScreen
import com.example.skillsharex.ui.profile.ProfileScreen
import com.example.skillsharex.ui.settings.SettingsScreen
import com.example.skillsharex.ui.splash.AppSplashScreen
import com.example.skillsharex.utils.SessionManager
import com.example.skillsharex.ui.login.LoginScreen
import com.example.skillsharex.ui.session.SessionListScreen
import com.example.skillsharex.ui.sessions.SessionDetailScreen
import com.example.skillsharex.ui.sessions.SessionScreen
import com.example.skillsharex.ui.signup.SignUpScreen
import com.example.skillsharex.ui.splash.OnboardingScreen
import androidx.navigation.navArgument
import com.example.skillsharex.ui.session.LiveSessionScreen
import com.example.skillsharex.ui.session.SessionOverviewScreen
import com.example.skillsharex.ui.call.CallScreen
import com.example.skillsharex.ui.requests.MentorshipRequestsScreen


@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val context = LocalContext.current
    val session = SessionManager(context)

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

        /* -------- login -------- */
        composable("login") {
            LoginScreen(
                onLoginSuccess = { userName: String ->
                    session.saveUserName(userName)
                    session.setLoggedIn(true)

                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onSignUpClick = {
                    navController.navigate("signup")
                },
                onForgotPasswordClick = {
                    navController.navigate("forgotPassword")
                }
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

        /* -------- MAIN APP -------- */
        composable("home") { HomeDashboardScreen(navController) }
        composable("mentors") { MentorListScreen(navController) }
        composable("community") { CommunityScreen(navController) }
        composable("createPost") { CreatePostScreen(navController) }
        composable("profile") { ProfileScreen(navController) }
        composable("settings") { SettingsScreen(navController) }
        composable("notifications") { NotificationsScreen(navController) }

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
        composable("createPost") {
            CreatePostScreen(navController)
        }
        composable(Screen.SkillFilter.route) {
            SkillFilterScreen(navController)
        }

        composable("chat") {
            ChatScreen(navController)
        }
        composable("call") {
            CallScreen(navController)
        }


        composable(
            route = "post_detail/{postTitle}",
            arguments = listOf(
                navArgument("postTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val postTitle =
                backStackEntry.arguments?.getString("postTitle") ?: ""

            PostDetailScreen(
                navController = navController,
                postTitle = postTitle
            )
        }
        composable("editProfile") {
            EditProfileScreen(navController)
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
        composable(
            "chat/{mentorId}",
            arguments = listOf(navArgument("mentorId") { type = NavType.StringType })
        ) {
            ChatScreen(navController)
        }
        composable(Screen.SessionList.route) {
            SessionListScreen(navController)
        }

        composable(
            route = "session_overview/{sessionId}",
            arguments = listOf(
                navArgument("sessionId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val sessionId = backStackEntry.arguments?.getString("sessionId")

            SessionOverviewScreen(
                navController = navController,
                sessionId = sessionId ?: ""
            )
        }
        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
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
    }
}

