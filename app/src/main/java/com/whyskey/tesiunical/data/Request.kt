package com.whyskey.tesiunical.data

data class Request(
    val id_student: String = "",
    val id_professor: String = "",
    val id_thesis: String = "",
    val name: String = "",
    val session: Int = 0,
    val accepted: Boolean = false,
    val thesis: String = "",
    var id: String = "",
    var image: String = "",
    val email: String = ""
)