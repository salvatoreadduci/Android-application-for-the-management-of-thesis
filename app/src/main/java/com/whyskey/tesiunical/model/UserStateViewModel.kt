package com.whyskey.tesiunical.model

import android.annotation.SuppressLint
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class UserStateViewModel : ViewModel() {
    var isLoggedIn by mutableStateOf(false)

    fun signIn() {
        isLoggedIn = true
    }
}

@SuppressLint("CompositionLocalNaming")
val UserState = compositionLocalOf<UserStateViewModel> { error("User State Context Not Found!") }

