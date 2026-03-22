package com.z.photos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.z.photos.navigation.PHOTO_ID_ARG
import com.z.photos.navigation.Routes
import com.z.photos.ui.detail.DetailScreen
import com.z.photos.ui.favorites.FavoritesScreen
import com.z.photos.ui.feed.FeedScreen
import com.z.photos.ui.main.BOTTOM_NAV_ROUTES
import com.z.photos.ui.main.BottomBar
import com.z.photos.ui.main.MainViewModel
import com.z.photos.ui.theme.HomeworkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HomeworkTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = hiltViewModel()
                val favoriteCount by mainViewModel.favoriteCount.collectAsState()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val showBottomBar = currentRoute in BOTTOM_NAV_ROUTES

                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomBar(
                                currentRoute = currentRoute,
                                favoriteCount = favoriteCount,
                                navController = navController,
                            )
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.FEED,
                        modifier = Modifier.padding(innerPadding),
                    ) {
                        composable(Routes.FEED) {
                            FeedScreen(
                                onPhotoClick = { photo ->
                                    navController.navigate(Routes.detail(photo.id))
                                },
                            )
                        }
                        composable(Routes.FAVORITES) {
                            FavoritesScreen(
                                onPhotoClick = { photo ->
                                    navController.navigate(Routes.detail(photo.id))
                                },
                            )
                        }
                        composable(
                            route = Routes.DETAIL,
                            arguments = listOf(navArgument(PHOTO_ID_ARG) { type = NavType.LongType }),
                        ) {
                            DetailScreen(
                                onBack = {
                                    navController.popBackStack()
                                },
                            )
                        }
                    }
                }
            }
        }
    }
}
