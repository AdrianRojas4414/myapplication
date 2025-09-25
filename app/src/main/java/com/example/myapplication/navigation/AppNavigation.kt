package com.example.myapplication.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.myapplication.features.dollar.presentation.DollarScreen
import com.example.myapplication.features.github.presentation.GithubScreen
import com.example.myapplication.features.login.presentation.LogInPage
import com.example.myapplication.features.movies.presentation.MoviesScreen

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.LogInPage.route,
        enterTransition = { EnterTransition.None},
        exitTransition = { ExitTransition.None},
        popEnterTransition = { EnterTransition.None},
        popExitTransition = { ExitTransition.None}
    ){
        composable(Screen.LogInPage.route){
            LogInPage(
                onSuccess = {
                    //navController.navigate(Screen.GithubScreen.route)
                    navController.navigate(Screen.Dollar.route)
                }
            )
        }

        //composable(Screen.GithubScreen.route) {
          //  GithubScreen(modifier = Modifier.padding())
        //}


        /*composable(Screen.MoviesScreen.route){
            MoviesScreen(modifier = Modifier.padding())
        }*/

        composable(Screen.Dollar.route) {
            DollarScreen()
        }

    }


    /*NavHost(
        navController = navController,
        startDestination = Screen.Dollar.route
    ) {
        composable(Screen.Github.route) {
            GithubScreen(modifier = Modifier)
        }
        composable(Screen.Home.route) {


        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }


        composable(Screen.CardExamples.route) { CardScreen() }


        composable(Screen.Dollar.route) {
            DollarScreen()
        }
    }*/

}