package com.whyskey.tesiunical.data

data class Request(
    val id_student: String = "",
    val id_professor: String = "",
    val id_thesis: String = "",
    val accepted: Boolean = false,
    var id: String = ""
)