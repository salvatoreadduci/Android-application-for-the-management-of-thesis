package com.whyskey.tesiunical

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

enum class ThesisScreen(
    val icon: ImageVector
    ) {
        Home(
            icon = Icons.Filled.Home
        ),
        Analytics(
            icon = Icons.Filled.Insights
        ),
        Profile(
            icon = Icons.Filled.Person
        ),
        Settings(
          icon = Icons.Filled.Settings
        ),
        CompilationFullScreen(
            icon = Icons.Filled.AttachMoney
        ),
        ExperimentalFullScreen(
            icon = Icons.Filled.MoneyOff
        );

        companion object {
            fun fromRoute(route: String?): ThesisScreen =
                when (route?.substringBefore("/")) {
                    Home.name -> Home
                    Analytics.name -> Analytics
                    Profile.name -> Profile
                    Settings.name -> Settings
                    CompilationFullScreen.name -> CompilationFullScreen
                    ExperimentalFullScreen.name -> ExperimentalFullScreen
                    null -> Home
                    else -> throw IllegalArgumentException("Route $route is not recognized.")
                }
        }
}