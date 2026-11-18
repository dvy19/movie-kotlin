package com.example.movie

import ChatScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movie.screens.AddPostScreen
import com.example.movie.screens.AllUsersScreen
import com.example.movie.screens.HomeScreen
import com.example.movie.screens.MovieFeedScreen
import com.example.movie.screens.MovieGridScreen
import com.example.movie.screens.ProfileScreen
import com.example.movie.screens.sampleMoviePosts
import com.example.movie.screens.sampleUser
import com.example.movie.screens.sampleUsers

@Composable
fun MainScreen(
    innerPadding: PaddingValues
) {
    val mainNavController = rememberNavController()

    val navBackStackEntry by mainNavController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    Scaffold(
        bottomBar = {
            BottomNav(mainNavController)
        }
    ) { paddingValues ->
        NavHost(
            modifier = Modifier.padding(paddingValues),
            navController = mainNavController,
            startDestination = Screens.HomeScreen.route
        ) {

            composable(Screens.HomeScreen.route) {
                HomeScreen(mainNavController)
            }

            composable(Screens.MovieGridScreen.route) {
                MovieGridScreen(
                    mainNavController,
                    modifier = Modifier,
                    movies = emptyList()
                )
            }

            composable(Screens.ChatScreen.route) {
                ChatScreen(
                    mainNavController,
                    communities = emptyList(),
                    chats = emptyList(),
                    onCommunityClick = { },
                    onChatClick = { },
                    onViewAllChatsClick = { }
                )


            }

            composable(Screens.MovieFeedScreen.route){
                MovieFeedScreen(
                    mainNavController,
                    posts = sampleMoviePosts,
                    onCommentClick = { },
                    onLikeToggle = { _, _ -> },
                    onNewPostClick = { mainNavController.navigate(Screens.AddPostScreen.route)}
                )
            }

            composable(Screens.ProfileScreen.route){
                ProfileScreen(
                    user = sampleUser,
                    userPosts = sampleMoviePosts,
                    onSettingsClick = { },
                    mainNavController

                )
            }


            composable(Screens.AllUsersScreen.route){
                AllUsersScreen(
                    mainNavController,
                    users = sampleUsers,
                    onMessageClick = { }
                )
            }

            composable(Screens.AddPostScreen.route) {
                AddPostScreen(
                    mainNavController=mainNavController,
                    onNavigateBack = { mainNavController.popBackStack() },
                )
            }


        }
    }
}