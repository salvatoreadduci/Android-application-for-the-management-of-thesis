package com.whyskey.tesiunical.data

data class Account(
    val name: String = "",
    val email: String = "",
    val web_site: String = "",
    var isProfessor: Boolean = true,
    val march_session: Map<String,Map<String,Int>> = emptyMap(),
    val july_session: Map<String,Map<String,Int>> = emptyMap(),
    val september_session: Map<String,Map<String,Int>> = emptyMap(),
    val december_session: Map<String,Map<String,Int>> = emptyMap(),
    val exams: String = "",
    var thesis: String = "",
    val session: Int = -1,
    var id: String = "",
    var image: String = ""
)