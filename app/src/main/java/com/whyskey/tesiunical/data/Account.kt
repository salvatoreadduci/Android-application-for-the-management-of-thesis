package com.whyskey.tesiunical.data

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

data class Account(
    val name: String = "",
    val email: String = "",
    val web_site: String = "",
    var isProfessor: Boolean = true,
    var exams: String = "",
    var id: String = "",
    var image: String = ""
)