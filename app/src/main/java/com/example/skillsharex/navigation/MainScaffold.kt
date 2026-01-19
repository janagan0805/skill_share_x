package com.example.skillsharex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.skillsharex.ui.chatbot.ChatbotScreen
import com.example.skillsharex.ui.components.BottomNavBar
import com.example.skillsharex.ui.community.CommunityScreen
import com.example.skillsharex.ui.community.CreatePostScreen
import com.example.skillsharex.ui.community.PostDetailScreen
import com.example.skillsharex.ui.course.CourseDetailScreen
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.mentorscreen.MentorDetailScreen
import com.example.skillsharex.ui.mentorscreen.MentorListScreen
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.profile.CreateCourseScreen
import com.example.skillsharex.ui.profile.EditCourseDetailScreen
import com.example.skillsharex.ui.profile.EditProfileScreen
import com.example.skillsharex.ui.profile.ProfileScreen
import com.example.skillsharex.ui.profile.UserCourseScreen
import com.example.skillsharex.ui.requests.MentorshipRequestsScreen
import com.example.skillsharex.ui.sessions.LiveSessionScreen
import com.example.skillsharex.ui.sessions.SessionOverviewScreen
import com.example.skillsharex.ui.sessions.SessionListScreen
import com.example.skillsharex.ui.settings.SettingsScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.skillsharex.viewmodel.MentorListViewModel
import com.example.skillsharex.viewmodel.SessionViewModel
import com.example.skillsharex.viewmodel.community.CommunityViewModel
import com.example.skillsharex.viewmodel.home.DashboardViewModel


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScaffold(
    rootNavController: NavController // 🔑 THIS FIXES LOGOUT
) {
    val bottomBarRoutes = setOf(
        Routes.HOME,
        Routes.COMMUNITY,
        Routes.SESSIONS,
        Routes.MENTORS,
        Routes.PROFILE
    )


    /* ---------- VIEWMODELS (TOP LEVEL) ---------- */
    val homeViewModel: DashboardViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()
    val mentorListViewModel: MentorListViewModel = viewModel()
    val sessionViewModel: SessionViewModel = viewModel()

    val context = LocalContext.current
    val mainNavController = rememberNavController()

    val navBackStackEntry =
        mainNavController.currentBackStackEntryAsState()

    val currentRoute =
        navBackStackEntry.value?.destination?.route

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Routes.HOME -> {
                homeViewModel.loadDashboardData(force = true)
            }

            Routes.COMMUNITY -> {
                communityViewModel.loadCommunityFeed(context, force = true)
            }

            Routes.MENTORS -> {
                mentorListViewModel.loadMentorsList(force = true)
            }

            Routes.SESSIONS -> {
                sessionViewModel.loadSessions(force = true)
            }
        }
    }


    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute in bottomBarRoutes,
//                enter = slideInVertically { fullHeight -> fullHeight } + fadeIn(),
//                exit = slideOutVertically { fullHeight -> fullHeight } + fadeOut(),
                enter = slideInVertically(
                    animationSpec = tween(300)
                ) { it } + fadeIn(),

                exit = slideOutVertically(
                    animationSpec = tween(250)
                ) { it } + fadeOut()

            ) {
                BottomNavBar(navController = mainNavController)
            }
        }
    )

    { innerPadding ->

        NavHost(
            navController = mainNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {

            composable(Routes.HOME) {
                HomeDashboardScreen(
                    navController = mainNavController,
                    viewModel = homeViewModel
                )
            }

            composable(Routes.COMMUNITY) {
                CommunityScreen(
                    navController = mainNavController,
                    viewModel = communityViewModel
                )
            }

            composable(Routes.MENTORS) {
                MentorListScreen(
                    navController = mainNavController,
                    viewModel = mentorListViewModel
                )
            }

            composable(Routes.SESSIONS) {
                SessionListScreen(
                    navController = mainNavController,
                    sessionViewModel = sessionViewModel
                )
            }

            composable(Routes.PROFILE) {
                // 🔑 ROOT CONTROLLER PASSED TO PROFILE
                ProfileScreen(
                    navController = mainNavController,
                    rootNavController = rootNavController
                )
            }

            composable(Routes.NOTIFICATIONS) {
                NotificationsScreen(mainNavController)
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(mainNavController)
            }

            composable(Routes.EDIT_PROFILE) {
                EditProfileScreen(mainNavController)
            }

            composable(Routes.MENTORSHIP_REQUESTS) {
                MentorshipRequestsScreen(mainNavController)
            }

            composable(Routes.MY_COURSES) {
                UserCourseScreen(mainNavController)
            }

            composable(Routes.CREATE_COURSE) {
                CreateCourseScreen(mainNavController)
            }

            composable(
                Routes.COURSE_DETAIL,
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) {
                CourseDetailScreen(mainNavController)
            }

            composable(
                Routes.EDIT_COURSE,
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) {
                EditCourseDetailScreen(mainNavController)
            }

            composable(
                Routes.MENTOR_DETAIL,
                arguments = listOf(navArgument("mentorId") { type = NavType.IntType })
            ) {
                val mentorId = it.arguments!!.getInt("mentorId")
                MentorDetailScreen(mainNavController, mentorId)
            }

            composable(Routes.CREATE_POST) {
                CreatePostScreen(mainNavController)
            }

            composable(
                Routes.POST_DETAIL,
                arguments = listOf(navArgument("postId") { type = NavType.StringType })
            ) {
                val postId = it.arguments!!.getString("postId")!!
                PostDetailScreen(mainNavController, postId)
            }

            composable(
                Routes.SESSION_OVERVIEW,
                arguments = listOf(
                    navArgument("sessionId") { type = NavType.IntType }
                )
            ) {
                val sessionId = it.arguments!!.getInt("sessionId")

                SessionOverviewScreen(mainNavController, sessionId)
            }

            composable(
                Routes.LIVE_SESSION,
                arguments = listOf(
                    navArgument("sessionId") { type = NavType.IntType }
                )
            ) {
                val sessionId = it.arguments!!.getInt("sessionId")

                LiveSessionScreen(mainNavController, sessionId.toString())
            }

            composable(Routes.CHATBOT) {
                ChatbotScreen(mainNavController)
            }

        }
    }
}
