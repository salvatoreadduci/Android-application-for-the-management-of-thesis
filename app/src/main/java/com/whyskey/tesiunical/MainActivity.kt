package com.whyskey.tesiunical

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.*
import com.whyskey.tesiunical.ui.components.AddThesisDialog
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
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
                ThesisApp(viewModel)
            }
        }
    }
}

@Composable
fun ThesisApp(viewModel: ThesisViewModel) {
    TesiUnicalTheme {

        val allThesis by viewModel.allThesis.observeAsState(listOf())
        val allCompilation by viewModel.allCompilation.observeAsState(listOf())
        val allExperimental by viewModel.allExperimental.observeAsState(listOf())
        val showDialogState: Boolean by viewModel.showDialog.collectAsState()
        //Navigation
        val allScreens = ThesisScreen.values().toList()
        //val allScreens =
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = ThesisScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )
        
        AddThesisDialog(show = showDialogState,
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
                allThesis = allThesis,
                allCompilation = allCompilation,
                allExperimental = allExperimental,
                viewModel = viewModel
            )

        }
    }
}

@Composable
fun ThesisNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    allThesis: List<Thesis>,
    allCompilation: List<Thesis>,
    allExperimental: List<Thesis>,
    viewModel: ThesisViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ThesisScreen.Home.name,
        modifier = modifier

    ) {
        composable(ThesisScreen.Home.name){
            Home(
                list = allThesis,
                viewModel = viewModel
            )
        }

        composable(ThesisScreen.Analytics.name) {
            Analytics()
        }

        composable(ThesisScreen.Profile.name) {
            Profile(
                onClickSeeAll = { name -> navigateToFullScreenThesis(navController, name) },
                allCompilation = allCompilation,
                allExperimental = allExperimental,
                viewModel = viewModel
            )
        }

        composable(ThesisScreen.Settings.name){
            Settings()
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
                    CompilationFullScreen(list = allCompilation, viewModel = viewModel)
                stringResource(id = R.string.experimental_thesis) ->
                    ExperimentalFullScreen(list = allExperimental, viewModel = viewModel)
            }

        }
    }
}

private fun navigateToFullScreenThesis(
    navController: NavHostController,
    listName: String
) {
    println(listName)
    navController.navigate("${ThesisScreen.Profile.name}/$listName")
}

class MainViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThesisViewModel(application) as T
    }
}

@Composable
private fun  AddFloatingActionButton(onClick: () -> Unit){
    FloatingActionButton(onClick = onClick) {
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