package com.ejrm.locationreminder.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ejrm.locationreminder.R
import com.ejrm.locationreminder.ui.screen.ReminderScreen
import com.ejrm.locationreminder.ui.screen.SaveLocationsScreen


@Composable
fun currentRoute(navController: NavHostController): String? {
    val navBackStackEntry = navController.currentBackStackEntryAsState().value
    return navBackStackEntry?.destination?.route
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem.MyLocation,
        NavigationItem.SaveLocations
    )
    NavigationBar {
        val currentRoute = currentRoute(navController)
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        ImageVector.vectorResource(id = item.icon),
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        navController.graph.startDestinationRoute?.let { route ->
                            popUpTo(route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController,
        startDestination = NavigationItem.MyLocation.route,
        modifier = modifier
    ) {
        composable(NavigationItem.MyLocation.route) {
            ReminderScreen()
        }
        composable(NavigationItem.SaveLocations.route) {
            SaveLocationsScreen()
        }
    }
}


sealed class NavigationItem(var route: String, var icon: Int, var title: String) {
    object MyLocation : NavigationItem("mylocation", R.drawable.ic_location_pin, "")
    object SaveLocations : NavigationItem("savedlocations", R.drawable.ic_bookmark, "")
}