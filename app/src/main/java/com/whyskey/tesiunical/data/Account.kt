package com.whyskey.tesiunical.data

data class Account(
    val name: String = "Prova",
    val email: String = "",
    val web_site: String = "",
    var isProfessor: Boolean = true,
    val max_compilation: Int = 0,
    val max_applicative: Int = 0,
    val max_research: Int = 0,
    val max_corporate: Int = 0,
    val max_erasmus: Int = 0,
    val exams: String = "",
    val thesis: String = "",
    var id: String = ""
)