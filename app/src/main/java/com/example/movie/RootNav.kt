package com.example.movie

import UserDetailScreen
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movie.auth.Login
import com.example.movie.auth.Register
import com.example.movie.auth.SplashScreen

@Composable
fun RootNav(innerPadding: PaddingValues) {
    val rootNavController = rememberNavController()

    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()

    NavHost(
        navController = rootNavController,
        startDestination = Screens.Login.route

    ) {

        composable(Screens.Splash.route){
            SplashScreen(rootNavController)
        }


        composable(Screens.Register.route){
            Register(
                rootNavController,
                onLoginClicked = { rootNavController.navigate(Screens.Login.route) }

            )
        }

        composable(Screens.Login.route){
            Login(
                rootNavController
            , onSignupClicked = { rootNavController.navigate(Screens.Register.route) }

            )
        }


        composable(Screens.UserDetailScreen.route){
            UserDetailScreen(rootNavController)
        }

        composable(Screens.MainScreen.route){
            MainScreen(innerPadding)

        }

    }

}