package com.whyskey.tesiunical


import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.*

@Composable
fun ThesisNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: ThesisViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
        modifier = modifier

    ) {
        composable(route = Home.route){
            Home(
                viewModel = viewModel,
                onClick = { account -> navController.navigateToSingleAccount(account) }
            )
        }

        composable(route = Analytics.route){
            Analytics(viewModel)
        }

        composable(route = Profile.route){
            Profile(
                onClickSeeAll = { thesis -> navController.navigateToFullScreenThesis(thesis) },
                viewModel,
                viewModel.userData.value.id
            )
        }

        composable(route = Settings.route){
            Settings(viewModel)
        }

        composable(
            route = FullScreenThesis.routeWithArgs,
            arguments = FullScreenThesis.arguments,
            deepLinks = FullScreenThesis.deepLinks
        ) { navBackStackEntry ->
            val thesisList =
                navBackStackEntry.arguments?.getString(FullScreenThesis.fullScreenIdArg)
            val list = when (thesisList) {
                stringResource(id = R.string.application_thesis) -> viewModel.applicationThesis.value
                stringResource(id = R.string.compilation_thesis) -> viewModel.compilationThesis.value
                else -> viewModel.researchThesis.value
            }
            ThesisFullScreen(
                list = list,
                viewModel = viewModel,
                title = thesisList!!
            )
        }

        composable(
            route = OtherProfile.routeWithArgs,
            arguments = OtherProfile.arguments,
            deepLinks = OtherProfile.deepLinks
        ) { navBackStackEntry ->
                val profileId = navBackStackEntry.arguments?.getString(OtherProfile.otherProfileIdArg)
            Log.d("TAG",profileId!!)
            Profile(
                onClickSeeAll = { thesis -> navController.navigateToFullScreenThesis(thesis) },
                viewModel,
                profileId!!
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }

fun NavHostController.navigateToSingleAccount(accountId: String) {
    this.navigateSingleTopTo("${Profile.route}/$accountId")
}

fun NavHostController.navigateToFullScreenThesis(ListId: String) {
    this.navigateSingleTopTo("${FullScreenThesis.route}/$ListId")
}