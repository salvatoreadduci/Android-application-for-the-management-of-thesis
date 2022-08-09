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
    val march_session: Map<String,List<Int>> = emptyMap(),
    val july_session: Map<String,List<Int>> = emptyMap(),
    val september_session: Map<String,List<Int>> = emptyMap(),
    val december_session: Map<String,List<Int>> = emptyMap(),
    val exams: String = "",
    var thesis: String = "",
    var id: String = "",
    var image: String = ""
)