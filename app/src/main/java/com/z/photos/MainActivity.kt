package com.z.photos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.z.photos.navigation.PHOTO_ID_ARG
import com.z.photos.navigation.Routes
import com.z.photos.ui.detail.DetailScreen
import com.z.photos.ui.feed.FeedScreen
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

                NavHost(navController = navController, startDestination = Routes.FEED) {
                    composable(Routes.FEED) {
                        FeedScreen(
                            onPhotoClick = { photo ->
                                navController.navigate(Routes.detail(photo.id))
                            }
                        )
                    }
                    composable(
                        route = Routes.DETAIL,
                        arguments = listOf(navArgument(PHOTO_ID_ARG) { type = NavType.LongType })
                    ) {
                        DetailScreen(
                            onBack = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
