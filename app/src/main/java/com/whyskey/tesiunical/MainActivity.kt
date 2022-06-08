package com.whyskey.tesiunical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.whyskey.tesiunical.ui.Profile
import com.whyskey.tesiunical.ui.ThesisFullScreen
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TesiUnicalTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ThesisApp()
                }
            }
        }
    }
}

@Composable
fun ThesisApp(){

    val allScreens = ThesisScreen.values().toList()
    val navController = rememberNavController()
    val backstackEntry = navController.currentBackStackEntryAsState()
    val currentScreen = ThesisScreen.fromRoute(
        backstackEntry.value?.destination?.route
    )

    Scaffold() { innerPadding ->
        ThesisNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )

    }

}


@Composable
fun ThesisNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = ThesisScreen.Profile.name,
        modifier = modifier

    ) {
        composable(ThesisScreen.Profile.name){
            Profile(
                onClickSeeAll = { navController.navigate(ThesisScreen.ThesisFullScreen.name) }
            )
        }

        composable(ThesisScreen.ThesisFullScreen.name){
            ThesisFullScreen(name = "SASA")
        }
    }
}
