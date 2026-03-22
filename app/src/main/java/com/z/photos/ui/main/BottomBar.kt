package com.z.photos.ui.main

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.z.photos.R
import com.z.photos.navigation.Routes

val BOTTOM_NAV_ROUTES = setOf(Routes.FEED, Routes.FAVORITES)

data class BottomNavTab(
    val route: String,
    @StringRes val labelResId: Int,
    val icon: @Composable () -> Unit,
)

fun buildBottomNavTabs(favoriteCount: Int) = listOf(
    BottomNavTab(
        route = Routes.FEED,
        labelResId = R.string.label_feed,
        icon = {
            Icon(
                imageVector = Icons.Filled.Home,
                contentDescription = null,
            )
        },
    ),
    BottomNavTab(
        route = Routes.FAVORITES,
        labelResId = R.string.label_favorites,
        icon = {
            if (favoriteCount > 0) {
                BadgedBox(
                    badge = { Badge { Text(favoriteCount.toString()) } }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null,
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                )
            }
        },
    ),
)

@Composable
fun BottomBar(
    currentRoute: String?,
    favoriteCount: Int,
    navController: NavController,
) {
    val tabs = buildBottomNavTabs(favoriteCount)

    NavigationBar {
        tabs.forEach { tab ->
            val isSelected = currentRoute == tab.route
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(tab.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = tab.icon,
                label = { Text(stringResource(tab.labelResId)) },
            )
        }
    }
}
