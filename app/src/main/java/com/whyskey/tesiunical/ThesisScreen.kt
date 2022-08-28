package com.whyskey.tesiunical

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink

interface Screen {
    val icon: ImageVector
    val route: String
}

object Home : Screen {
    override val icon = Icons.Filled.Home
    override val route = "home"
}

object Analytics : Screen {
    override val icon = Icons.Filled.Insights
    override val route = "statistiche"
}

object Profile : Screen {
    override val icon = Icons.Filled.Person
    override val route = "profilo"
}

object Settings : Screen {
    override val icon = Icons.Filled.Settings
    override val route = "impostazioni"
}

object FullScreenThesis : Screen {
    override val icon = Icons.Filled.Person
    override val route = "full_screen"
    const val fullScreenIdArg = "fullScreen_id"
    val routeWithArgs = "$route/{$fullScreenIdArg}"
    val arguments = listOf(
        navArgument(fullScreenIdArg) {
            type = NavType.StringType
        }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "thesis://$route/{$fullScreenIdArg}" }
    )
}

object OtherProfile : Screen {
    override val icon = Icons.Filled.Person
    override val route = "profilo"
    const val otherProfileIdArg = "profile_id"
    val routeWithArgs = "$route/{$otherProfileIdArg}"
    val arguments = listOf(
        navArgument(otherProfileIdArg) {
            type = NavType.StringType
        }
    )
    val deepLinks = listOf(
        navDeepLink { uriPattern = "thesis://$route/{$otherProfileIdArg}" }
    )
}

object Login : Screen {
    override val icon = Icons.Filled.Person
    override val route = "login"
}

object Register : Screen {
    override val icon = Icons.Filled.Person
    override val route = "register"
}

val tabScreens = listOf(Home,Analytics,Profile,Settings)
val tabScreensStudent = listOf(Home,Profile,Settings)