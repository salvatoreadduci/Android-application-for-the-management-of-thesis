package com.whyskey.tesiunical

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.Profile
import com.whyskey.tesiunical.ui.ThesisFullScreen
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

        //val allThesis by viewModel.allThesis.observeAsState(listOf())
        val allCompilation by viewModel.allCompilation.observeAsState(listOf())
        val allExperimental by viewModel.allExperimental.observeAsState(listOf())
        val showDialogState: Boolean by viewModel.showDialog.collectAsState()
        //Navigation
        //val allScreens = ThesisScreen.values().toList()
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
            floatingActionButton = {
                AddFloatingActionButton(
                    onClick = { viewModel.onOpenDialogClicked() }
                )
            }
        ) { innerPadding ->
            ThesisNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
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
    allCompilation: List<Thesis>,
    allExperimental: List<Thesis>,
    viewModel: ThesisViewModel
) {
    NavHost(
        navController = navController,
        startDestination = ThesisScreen.Profile.name,
        modifier = modifier

    ) {
        composable(ThesisScreen.Profile.name) {
            Profile(
                onClickSeeAll = { navController.navigate(ThesisScreen.ThesisFullScreen.name) },
                allCompilation = allCompilation,
                allExperimental = allExperimental,
                viewModel = viewModel
            )
        }
        composable(ThesisScreen.ThesisFullScreen.name) {
            ThesisFullScreen(
                list = allCompilation,
                viewModel = viewModel
            )
        }
    }
}

class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {
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