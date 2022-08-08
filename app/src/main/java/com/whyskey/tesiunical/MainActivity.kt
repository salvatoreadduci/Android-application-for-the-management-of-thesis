package com.whyskey.tesiunical

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.model.UserState
import com.whyskey.tesiunical.model.UserStateViewModel
import com.whyskey.tesiunical.ui.*
import com.whyskey.tesiunical.ui.components.AddThesisDialog
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        //Firebase.firestore.clearPersistence()
        super.onCreate(savedInstanceState)
        setContent {

            val owner = LocalViewModelStoreOwner.current

            owner?.let {
                val viewModel: ThesisViewModel = viewModel(
                    it,
                    "MainViewModel",
                    MainViewModelFactory(
                        LocalContext.current.applicationContext
                                as Application
                    )
                )
                if( viewModel.user != null){
                    userState.isLoggedIn = true
                }

                CompositionLocalProvider(UserState provides userState) {
                    ApplicationSwitcher(viewModel)
                }
            }
        }
    }
}

@Composable
fun ApplicationSwitcher(viewModel: ThesisViewModel) {
    val vm = UserState.current
    if (vm.isLoggedIn) {
        if(viewModel.userData.value.isProfessor){
            ThesisApp(viewModel)
        } else {
            ThesisAppStudent(viewModel = viewModel)
        }

    } else {
        Login(viewModel)
    }
}

@Composable
fun ThesisApp(viewModel: ThesisViewModel) {
    TesiUnicalTheme {

        //Navigation
        val allScreens = ThesisScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = ThesisScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )

        AddThesisDialog(
            show = viewModel.showDialog.collectAsState().value,
            onDismiss = viewModel::onDialogDismiss,
            onConfirm = viewModel::addNewThesis,
            viewModel = viewModel
        )
        
        Scaffold(
            topBar = {
                com.whyskey.tesiunical.ui.components.TabRow(
                    allScreens =  allScreens,
                    onTabSelected = { screen -> navController.navigate(screen.name) },
                    currentScreen = currentScreen
                )
            },
            floatingActionButton = {
                AddFloatingActionButton(
                    onClick = { viewModel.onOpenDialogClicked() }
                )
            }
        ) { innerPadding ->
            ThesisNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                allCompilation = viewModel.compilationThesis.value,
                allExperimental = viewModel.applicationThesis.value,
                allResearch = viewModel.researchThesis.value,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ThesisAppStudent(viewModel: ThesisViewModel) {
    TesiUnicalTheme {

        //Navigation
        val allScreens = listOf(ThesisScreen.Home,ThesisScreen.Profile, ThesisScreen.Settings)
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = ThesisScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )

        Scaffold(
            topBar = {
                com.whyskey.tesiunical.ui.components.TabRow(
                    allScreens =  allScreens,
                    onTabSelected = { screen -> navController.navigate(screen.name) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            ThesisNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                allCompilation = viewModel.compilationThesis.value,
                allExperimental = viewModel.applicationThesis.value,
                allResearch = viewModel.researchThesis.value,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ThesisNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    allCompilation: List<Thesis>,
    allExperimental: List<Thesis>,
    allResearch: List<Thesis>,
    viewModel: ThesisViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ThesisScreen.Home.name,
        modifier = modifier

    ) {

        val accountTypeArg = "account_id"
        val routeWithArgs = "${ThesisScreen.Profile.name}/{$accountTypeArg}"
        val arguments = listOf(
            navArgument(accountTypeArg) { type = NavType.StringType }
        )
        val deepLinks = listOf(
            navDeepLink { uriPattern = "thesis://${ThesisScreen.Profile.name}/{$accountTypeArg}" }
        )

        composable(
            route = routeWithArgs,
            arguments = arguments,
            deepLinks = deepLinks
        ) { navBackStackEntry ->
            val accountType =
                navBackStackEntry.arguments?.getString(accountTypeArg)
            Profile(
                onClickSeeAll = { name -> navigateToFullScreenThesis(navController, name) },
                allCompilation = allCompilation,
                allExperimental = allExperimental,
                allResearch = allResearch,
                viewModel = viewModel,
                id = accountType!!
            )
        }

        composable(ThesisScreen.Home.name){
            Home(
                onClick = { id -> navController.navigateToProfile(id) },
                viewModel = viewModel
            )
        }

        if(viewModel.userData.value.isProfessor){
            composable(ThesisScreen.Analytics.name) {
                Analytics(
                    viewModel = viewModel
                )
            }
        }

        composable(ThesisScreen.Profile.name) {
            Profile(
                onClickSeeAll = { name -> navigateToFullScreenThesis(navController, name) },
                allCompilation = allCompilation,
                allExperimental = allExperimental,
                allResearch = allResearch,
                viewModel = viewModel,
                id = viewModel.userData.value.id
            )
        }

        composable(ThesisScreen.Settings.name){
            Settings(viewModel = viewModel)
        }

        val profileName = ThesisScreen.Profile.name
        composable(
            "$profileName/{name}",
            arguments = listOf(
                navArgument("name"){
                    type = NavType.StringType
                }
            ),
            deepLinks = listOf(navDeepLink {
                uriPattern = "thesis://$profileName/{name}"
            })
        ){
                entry ->
            when(entry.arguments?.getString("name")){
                stringResource(id = R.string.compilation_thesis) ->
                    ThesisFullScreen(
                        list = allCompilation,
                        viewModel = viewModel,
                        title = stringResource(id = R.string.compilation_thesis)
                    )
                stringResource(id = R.string.application_thesis) ->
                    ThesisFullScreen(
                        list = allExperimental,
                        viewModel = viewModel,
                        title = stringResource(id = R.string.application_thesis)
                    )
                stringResource(id = R.string.research_thesis) ->
                    ThesisFullScreen(
                        list = allResearch,
                        viewModel = viewModel,
                        title = stringResource(id = R.string.research_thesis)
                    )
            }
        }
    }
}

private fun navigateToFullScreenThesis(
    navController: NavHostController,
    listName: String
) {
    navController.navigate("${ThesisScreen.Profile.name}/$listName")
}

private fun NavHostController.navigateToProfile(
    id: String
) {
    this.navigateSingleTopTo("${ThesisScreen.Profile.name}/$id")
}

@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThesisViewModel() as T
    }
}

@Composable
private fun  AddFloatingActionButton(onClick: () -> Unit){
    FloatingActionButton(onClick = onClick,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = null
            )
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        // Pop up to the start destination of the graph to
        // avoid building up a large stack of destinations
        // on the back stack as users select items
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        // Avoid multiple copies of the same destination when
        // reselecting the same item
        launchSingleTop = true
        // Restore state when reselecting a previously selected item
        restoreState = true
    }