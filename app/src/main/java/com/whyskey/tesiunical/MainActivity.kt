package com.whyskey.tesiunical

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
import java.time.LocalDate
import java.time.Month

class MainActivity : ComponentActivity() {
    private val userState by viewModels<UserStateViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        //Firebase.firestore.clearPersistence()
        super.onCreate(savedInstanceState)
        setContent {
            val owner = LocalViewModelStoreOwner.current
            owner?.let {
                val viewModel: ThesisViewModel = viewModel()
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

        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = tabScreens.find { it.route == currentDestination?.route } ?: Home

        AddThesisDialog(
            show = viewModel.showDialog.collectAsState().value,
            onDismiss = viewModel::onDialogDismiss,
            onConfirm = viewModel::addNewThesis,
            viewModel = viewModel
        )

        Scaffold(
            topBar = {
                com.whyskey.tesiunical.ui.components.TabRow(
                    allScreens =  tabScreens,
                    onTabSelected = { screen ->
                        navController.navigateSingleTopTo(screen.route) },
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
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun ThesisAppStudent(viewModel: ThesisViewModel) {
    TesiUnicalTheme {

        //Navigation
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackStack?.destination
        val currentScreen = tabScreensStudent.find { it.route == currentDestination?.route } ?: Home
        Scaffold(
            topBar = {
                com.whyskey.tesiunical.ui.components.TabRow(
                    allScreens =  tabScreensStudent,
                    onTabSelected = { screen ->
                        navController.navigateSingleTopTo(screen.route) },
                    currentScreen = currentScreen
                )
            }
        ) { innerPadding ->
            ThesisNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun AddFloatingActionButton(onClick: () -> Unit){
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