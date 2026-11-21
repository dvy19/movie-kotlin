package com.example.movie

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.example.movie.ui.theme.MovieTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    //RootNav(innerPadding)
                    MainScreen(

                        innerPadding
                    )
                }
            }
        }
    }
}

sealed class Screens(val route: String){

    data object Splash: Screens("splash")
    object Register: Screens("register")
    object Login: Screens("login")
    object HomeScreen: Screens("home")

    object MainScreen:Screens("main")

    data object UserDetailScreen: Screens("user_detail")

    data object MovieGridScreen:Screens("movie_grid_screen")

    data object MovieDetailScreen:Screens("movie_detail/{movie_id}")

    data object ChatScreen:Screens("chat_screen")

    data object AddPostScreen:Screens("add_post")

    data object AllUsersScreen:Screens("all_users")

    data object MovieFeedScreen:Screens("movie_feed")

    data object ProfileScreen:Screens("profile")

    data object SurveyDetailScreen:Screens("survey_details")






}