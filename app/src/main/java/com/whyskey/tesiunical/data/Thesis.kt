package com.whyskey.tesiunical.data

data class Thesis(
    val title: String = "",
    val type: Int = 0,
    val description: String = "",
    var id: String = "",
    var id_professor: String = "",
    var available: Boolean = true
)