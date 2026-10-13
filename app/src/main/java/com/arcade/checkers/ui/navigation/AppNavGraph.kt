package com.arcade.checkers.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arcade.checkers.game.CheckersGame
import com.arcade.checkers.ui.screens.GameScreen
import com.arcade.checkers.ui.screens.StatsScreen
import com.arcade.checkers.ui.screens.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val GAME = "game"
    const val STATS = "stats"
}

@Composable
fun AppNavGraph() {
    val navController = rememberNavController()
    val game = CheckersGame()

    NavHost(navController = navController, startDestination = Routes.WELCOME) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onStartGame = {
                    game.reset()
                    navController.navigate(Routes.GAME)
                },
                onNavigateStats = { navController.navigate(Routes.STATS) }
            )
        }
        composable(Routes.GAME) {
            GameScreen(
                game = game,
                onBack = { navController.popBackStack() },
                onStats = { navController.navigate(Routes.STATS) }
            )
        }
        composable(Routes.STATS) {
            StatsScreen(onBack = { navController.popBackStack() })
        }
    }
}
