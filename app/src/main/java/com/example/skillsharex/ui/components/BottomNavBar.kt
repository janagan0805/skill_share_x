package com.example.skillsharex.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.skillsharex.navigation.BottomNavItem

/**
 * A stateless, reusable bottom navigation bar component.
 *
 * @param navController The NavController that manages the state of the bottom navigation.
 */
@Composable
fun BottomNavBar(navController: NavController) {
    // List of items to display in the bottom navigation bar.
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Community,
        BottomNavItem.Sessions,
        BottomNavItem.Mentors,
        BottomNavItem.Profile
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                label = { Text(item.label) },
                icon = { Icon(item.icon, contentDescription = item.label) },
                onClick = {
                    navController.navigate(item.route) {
                        // Pop up to the start destination of the graph to
                        // avoid building up a large stack of destinations
                        // on the back stack as users select items.
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        // Avoid multiple copies of the same destination when
                        // re-selecting the same item.
                        launchSingleTop = true
                        // Restore state when re-selecting a previously selected item.
                        restoreState = true
                    }
                }
            )
        }
    }
}
