package com.example.movie

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.movie.auth.Login
import com.example.movie.auth.Register

@Composable
fun RootNav(innerPadding: PaddingValues) {
    val rootNavController = rememberNavController()

    val navBackStackEntry by rootNavController.currentBackStackEntryAsState()

    NavHost(
        navController = rootNavController,
        startDestination = Screens.Register.route

    ) {

        composable(Screens.Register.route){
            Register(rootNavController)
        }

        composable(Screens.Login.route){
            Login(rootNavController)
        }

    }

}