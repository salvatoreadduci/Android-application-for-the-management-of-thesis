package com.whyskey.tesiunical.data

import androidx.compose.ui.res.stringResource

data class Session(
    val applicative: Map<String,Int> = emptyMap(),
    val compilation: Map<String,Int> = emptyMap(),
    val corporate: Map<String,Int> = emptyMap(),
    val erasmus: Map<String,Int> = emptyMap(),
    val research: Map<String,Int> = emptyMap()
)