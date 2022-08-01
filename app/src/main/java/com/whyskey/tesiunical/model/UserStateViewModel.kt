package com.whyskey.tesiunical.model

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class UserStateViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)
    var isBusy by mutableStateOf(false)

    fun signIn(email: String, password: String) {
        isLoggedIn = true
    }

    suspend fun signOut() {
        isBusy = true
        delay(2000)
        isLoggedIn = false
        isBusy = false
    }
}

val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }

