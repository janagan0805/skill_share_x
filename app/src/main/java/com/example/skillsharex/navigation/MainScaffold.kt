package com.example.skillsharex.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.lifecycle.viewmodel.compose.viewModel

import com.example.skillsharex.ui.chatbot.ChatbotScreen
import com.example.skillsharex.ui.components.BottomNavBar
import com.example.skillsharex.ui.community.*
import com.example.skillsharex.ui.course.*
import com.example.skillsharex.ui.home.HomeDashboardScreen
import com.example.skillsharex.ui.mentorscreen.*
import com.example.skillsharex.ui.notifications.NotificationsScreen
import com.example.skillsharex.ui.profile.*
import com.example.skillsharex.ui.requests.MentorshipRequestsScreen
import com.example.skillsharex.ui.sessions.*
import com.example.skillsharex.ui.settings.SettingsScreen
import com.example.skillsharex.ui.subscription.SubscriptionScreen

import com.example.skillsharex.viewmodel.*
import com.example.skillsharex.viewmodel.community.CommunityViewModel
import com.example.skillsharex.viewmodel.home.DashboardViewModel

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun MainScaffold(
    rootNavController: NavController
) {

    val bottomBarRoutes = setOf(
        Routes.HOME,
        Routes.COMMUNITY,
        Routes.SESSIONS,
        Routes.MENTORS,
        Routes.PROFILE
    )

    val homeViewModel: DashboardViewModel = viewModel()
    val communityViewModel: CommunityViewModel = viewModel()
    val mentorListViewModel: MentorListViewModel = viewModel()
    val sessionViewModel: SessionViewModel = viewModel()

    val context = LocalContext.current
    val mainNavController = rememberNavController()

    val currentRoute =
        mainNavController.currentBackStackEntryAsState().value?.destination?.route

    LaunchedEffect(currentRoute) {
        when (currentRoute) {
            Routes.HOME -> homeViewModel.loadDashboardData(true)
            Routes.COMMUNITY -> communityViewModel.loadCommunityFeed(context, true)
            Routes.MENTORS -> mentorListViewModel.loadMentorsList(true)
            Routes.SESSIONS -> sessionViewModel.loadSessions(true)
        }
    }

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = currentRoute in bottomBarRoutes,
                enter = slideInVertically(tween(300)) { it } + fadeIn(),
                exit = slideOutVertically(tween(250)) { it } + fadeOut()
            ) {
                BottomNavBar(navController = mainNavController)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = mainNavController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding)
        ) {

            /* ---------- HOME ---------- */
            composable(Routes.HOME) {
                HomeDashboardScreen(mainNavController, homeViewModel)
            }

            /* ---------- COMMUNITY ---------- */
            composable(Routes.COMMUNITY) {
                CommunityScreen(mainNavController, communityViewModel)
            }

            /* ---------- MENTORS ---------- */
            composable(Routes.MENTORS) {
                MentorListScreen(mainNavController, mentorListViewModel)
            }

            /* ---------- SESSIONS ---------- */
            composable(Routes.SESSIONS) {
                SessionListScreen(mainNavController, sessionViewModel)
            }

            /* ---------- PROFILE ---------- */
            composable(Routes.PROFILE) {
                ProfileScreen(mainNavController, rootNavController)
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

            /* ---------- COURSE ---------- */
            composable(
                "${Routes.COURSE_DETAIL}/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) {
                CourseDetailScreen(mainNavController)
            }

            composable(
                "${Routes.EDIT_COURSE}/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) {
                EditCourseDetailScreen(mainNavController)
            }

            composable(
                "${Routes.ENROLLED_COURSE}/{courseId}",
                arguments = listOf(navArgument("courseId") { type = NavType.IntType })
            ) {
                EnrolledCourseScreen(mainNavController)
            }

            /* ---------- MENTOR ---------- */
            composable("${Routes.MENTOR_DETAIL}/{mentorId}") {
                val mentorId = it.arguments!!.getInt("mentorId")
                MentorDetailScreen(mainNavController, mentorId)
            }

            /* ---------- POSTS ---------- */
            composable(Routes.CREATE_POST) {
                CreatePostScreen(mainNavController)
            }

            composable("${Routes.POST_DETAIL}/{postId}") {
                val postId = it.arguments!!.getString("postId")!!
                PostDetailScreen(mainNavController, postId)
            }

            /* ---------- SESSIONS ---------- */
            composable("${Routes.SESSION_OVERVIEW}/{sessionId}") {
                val sessionId = it.arguments!!.getInt("sessionId")
                SessionOverviewScreen(mainNavController, sessionId)
            }

            composable("${Routes.LIVE_SESSION}/{sessionId}") {
                val sessionId = it.arguments!!.getInt("sessionId")
                LiveSessionScreen(mainNavController, sessionId.toString())
            }

            /* ---------- CHATBOT ---------- */
            composable(Routes.CHATBOT) {
                ChatbotScreen(mainNavController)
            }

            /* ---------- SUBSCRIPTION (SAFE VERSION) ---------- */
            composable("subscription") {
                SubscriptionScreen(mainNavController) // ✅ NO VIEWMODEL
            }
        }
    }
}