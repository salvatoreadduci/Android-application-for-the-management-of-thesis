package com.whyskey.tesiunical.ui

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Card
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.whyskey.tesiunical.data.Account
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.ProfileItem

@Composable
fun Home(
    viewModel: ThesisViewModel,
    onClick: (String) -> Unit = {}
) {
    viewModel.getAccountsByType(true)
    HomeBody(viewModel.accounts.value, viewModel,onClick)
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
    LazyVerticalGrid(
         GridCells.Fixed(2)
    ){
        itemsIndexed(accounts) { _, item ->
            ProfileItem(profile = item, onClick = { onClick(item.id)})
        }
    }
}