package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable

enum class Type{
    COMPILATION,
    APPLICATION,
    RESEARCH,
    CORPORATE,
    ERASMUS
}

class DataType(
    val name: Type,
    var amount: Int,
    var max: Int
)

data class Thesis(
    val title: String = "",
    val type: Int = 0,
    val description: String = ""
)