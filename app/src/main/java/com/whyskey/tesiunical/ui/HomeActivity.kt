package com.whyskey.tesiunical.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Account
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.AccountCollection
import com.whyskey.tesiunical.ui.components.ProfileItem

@Composable
fun Home(
    viewModel: ThesisViewModel,
    onClick: (String) -> Unit = {}
) {

    if(viewModel.userData.value.isProfessor){
        viewModel.getAccountsByType(false)
    } else {
        viewModel.getAccountsByType(true)
    }
    val temp = viewModel.accounts.value + viewModel.accountsToAccept.value

    HomeBody(temp, viewModel,onClick)
}
@Composable
private fun HomeBody(
    accounts: List<Account>,
    viewModel: ThesisViewModel,
    onClick: (String) -> Unit
){
    Card(modifier = Modifier.fillMaxSize()) {
            ProfilesCollectionList(accounts = accounts, viewModel,onClick)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProfilesCollectionList(
    accounts: List<Account>,
    viewModel: ThesisViewModel,
    onClick: (String) -> Unit
){
    if(!viewModel.userData.value.isProfessor){
        LazyVerticalGrid(
            GridCells.Fixed(2)
        ){
            itemsIndexed(accounts) { _, item ->
                ProfileItem(profile = item,viewModel = viewModel, onClick = { onClick(item.id)})
            }
        }
    } else {
        var temp = true
        LazyColumn(contentPadding = PaddingValues(start = 12.dp, end = 12.dp)){

            items(5){
                if(temp){
                    AccountCollection(title = stringResource(id = R.string.students_to_follow), profileCollection = viewModel.accountsToAccept.value,viewModel = viewModel, onClick)
                    temp = false
                } else {
                    Divider()
                    AccountCollection(title = stringResource(id = R.string.followed_students),profileCollection = viewModel.accounts.value,viewModel = viewModel, onClick)
                }
            }
        }
    }
}