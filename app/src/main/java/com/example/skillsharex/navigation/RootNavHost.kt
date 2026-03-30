package com.example.skillsharex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.*

import com.example.skillsharex.ui.forgot.ForgotPasswordScreen
import com.example.skillsharex.ui.login.LoginScreen
import com.example.skillsharex.ui.signup.SignUpScreen
import com.example.skillsharex.ui.splash.AppSplashScreen
import com.example.skillsharex.ui.splash.OnboardingScreen
import com.example.skillsharex.utils.SessionManager

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun RootNavHost() {

    val rootNavController = rememberNavController()
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }

    NavHost(
        navController = rootNavController,
        route = Routes.ROOT_GRAPH,
        startDestination = Routes.SPLASH
    ) {

        composable(Routes.SPLASH) {
            AppSplashScreen {
                val destination = when {
                    sessionManager.isFirstLaunch() -> Routes.ONBOARDING
                    sessionManager.isLoggedIn() -> Routes.MAIN_GRAPH
                    else -> Routes.AUTH_GRAPH
                }

                rootNavController.navigate(destination) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                }
            }
        }

        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onFinish = {
                    sessionManager.setFirstLaunchDone()
                    rootNavController.navigate(Routes.AUTH_GRAPH) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        navigation(
            route = Routes.AUTH_GRAPH,
            startDestination = Routes.LOGIN
        ) {

            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        sessionManager.setLoggedIn(true)
                        rootNavController.navigate(Routes.MAIN_GRAPH) {
                            popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                        }
                    },
                    onSignUpClick = { rootNavController.navigate(Routes.SIGNUP) },
                    onForgotPasswordClick = { rootNavController.navigate(Routes.FORGOT_PASSWORD) }
                )
            }

            composable(Routes.SIGNUP) {
                SignUpScreen(
                    onSignUpSuccess = { rootNavController.popBackStack() },
                    onBackToLogin = { rootNavController.popBackStack() }
                )
            }

            composable(Routes.FORGOT_PASSWORD) {
                ForgotPasswordScreen {
                    rootNavController.popBackStack()
                }
            }
        }

        composable(Routes.MAIN_GRAPH) {
            MainScaffold(rootNavController)
        }
    }
}