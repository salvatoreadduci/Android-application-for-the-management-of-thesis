package com.whyskey.tesiunical

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

        val allScreens = ThesisScreen.values().toList()
        val navController = rememberNavController()
        val backstackEntry = navController.currentBackStackEntryAsState()
        val currentScreen = ThesisScreen.fromRoute(
            backstackEntry.value?.destination?.route
        )
        Scaffold() { innerPadding ->
            ThesisNavHost(
                navController = navController,
                modifier = Modifier.padding(innerPadding),
                list = allThesis
            )
        }
    }
}


@Composable
fun ThesisNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    list: List<Thesis>
) {


    NavHost(
        navController = navController,
        startDestination = ThesisScreen.Profile.name,
        modifier = modifier

    ) {
        composable(ThesisScreen.Profile.name) {
            Profile(
                onClickSeeAll = { navController.navigate(ThesisScreen.ThesisFullScreen.name) },
                list = list
            )
        }
        composable(ThesisScreen.ThesisFullScreen.name) {
            ThesisFullScreen(list = list)
        }
    }
}


class MainViewModelFactory(val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ThesisViewModel(application) as T
    }
}
