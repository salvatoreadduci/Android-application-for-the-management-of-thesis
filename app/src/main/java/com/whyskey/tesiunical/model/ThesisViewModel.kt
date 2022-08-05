package com.whyskey.tesiunical.model

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.*
import androidx.core.net.toUri
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.whyskey.tesiunical.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File

class ThesisViewModel : ViewModel(){

    private var _auth: FirebaseAuth = Firebase.auth
    val auth = _auth

    private var _storage = Firebase.storage.reference
    val storage = _storage

    private var _user = auth.currentUser
    val user = _user

    private val _compilationThesis = mutableStateOf<List<Thesis>>(emptyList())
    val compilationThesis: State<List<Thesis>>
        get() = _compilationThesis

    private val _applicationThesis = mutableStateOf<List<Thesis>>(emptyList())
    val applicationThesis: State<List<Thesis>>
        get() = _applicationThesis

    private val _researchThesis = mutableStateOf<List<Thesis>>(emptyList())
    val researchThesis: State<List<Thesis>>
        get() = _researchThesis

    private val _corporateThesis = mutableStateOf<List<Thesis>>(emptyList())
    val corporateThesis: State<List<Thesis>>
        get() = _corporateThesis

    private val _erasmusThesis = mutableStateOf<List<Thesis>>(emptyList())
    val erasmusThesis: State<List<Thesis>>
        get() = _erasmusThesis

    private val _userData = mutableStateOf(Account())
    val userData: State<Account>
        get() = _userData

    private val _userImage = mutableStateOf<Uri?>(null)
    val userImage: State<Uri?>
        get() = _userImage

    private val _accounts = mutableStateOf<List<Account>>(emptyList())
    val accounts: State<List<Account>>
        get() = _accounts

    init {
        if(user != null){
            getAllData()
        }
    }

    fun setExams(exams: String){
        updateExams(exams)
    }

    private fun updateExams(exams:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!.uid)
                .update(mapOf(
                    "exams" to exams
                ))
        }
    }

    fun getImage(){
        retrieveImage()
    }

    fun getAllData(){
        getUserData(Firebase.auth.currentUser?.uid)

        getImage()

        if(_userData.value.isProfessor){
            getThesis(_userData.value.id,0)
            getThesis(_userData.value.id,1)
            getThesis(_userData.value.id,2)
            getThesis(_userData.value.id,3)
            getThesis(_userData.value.id,4)
        }
    }

    fun getAccountsByType(type: Boolean){
        getAccounts(type)
    }

    private fun getAccounts(type: Boolean){
        viewModelScope.launch {
            Firebase.firestore.collection("account").whereEqualTo("isProfessor",type).addSnapshotListener { value, _ ->
                if(value != null) {
                    val accounts = ArrayList<Account>()
                    val documents = value.documents
                    documents.forEach {
                        val temp = it.toObject(Account::class.java)
                        if(temp != null){
                            temp.id = it.id
                            accounts.add(temp)
                        }
                    }
                    _accounts.value = accounts
                }
            }
        }
    }

    private fun retrieveImage(){
        val localFile: File = File.createTempFile("localFile", ".jpg")
        viewModelScope.launch {
            storage.child("images/" + user?.uid).getFile(localFile).addOnSuccessListener {

                val uri: Uri = localFile.absolutePath.toUri()
                _userImage.value = uri
            }
        }
    }

    fun removeThesis(thesis: String){
        deleteThesis(thesis)
    }

    private fun deleteThesis(thesis: String){
        viewModelScope.launch {

            Firebase.firestore.collection("thesis").document(thesis)
                .delete()
                .addOnSuccessListener { Log.d("TAG", "DocumentSnapshot successfully deleted!") }
                .addOnFailureListener { e -> Log.w("TAG", "Error deleting document", e) }
        }
    }

    fun changeName(name:String){
        setName(name)
        _showOptionNameDialog.value = false
    }

    private fun setName(name:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!.uid)
                .update(mapOf(
                    "name" to name
                ))
        }
    }

    fun changeEmail(email:String){
        setEmail(email)
        _showOptionEmailDialog.value = false
    }

    private fun setEmail(email:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!.uid)
                .update(mapOf(
                    "email" to email
                ))
        }
    }

    fun changeLimit(type: String, value: Int){

        val temp = when(type){
            "Tesi Compilativa" -> "max_compilation"
            "Tesi Applicativa" -> "max_applicative"
            "Tesi di ricerca" -> "max_research"
            "Tesi in azienda" -> "max_corporate"
            else -> "max_erasmus"
        }
        setLimit(temp,value)
    }

    private fun setLimit(type: String, value: Int){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!.uid)
                .update(mapOf(
                    type to value
                ))
        }
    }

    fun changeWebSite(webSite:String){
        setWebSite(webSite)
        _showOptionWebDialog.value = false
    }

    private fun setWebSite(webSite:String){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!.uid)
                .update(mapOf(
                    "web_site" to webSite
                ))
        }
    }

    fun getThesis(id:String,type: Int){

        viewModelScope.launch {
            Firebase.firestore.collection("thesis").whereEqualTo("type",type).whereEqualTo("id_professor",id).addSnapshotListener { value, _ ->
                if(value != null) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents
                    documents.forEach {
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            thesis.add(temp)
                        }
                    }
                    Log.d("TAG",thesis.toString())
                    when(type){
                        0 -> _compilationThesis.value = thesis
                        1 -> _applicationThesis.value = thesis
                        2 -> _researchThesis.value = thesis
                        3 -> _corporateThesis.value = thesis
                        4 -> _erasmusThesis.value = thesis
                    }
                }
            }

        }
    }

    private fun getUserData(user: String?){
        viewModelScope.launch {
            Firebase.firestore.collection("account").document(user!!)
                .addSnapshotListener { value, _ ->
                if(value != null) {
                    _userData.value = value.toObject()!!
                    _userData.value.id = user
                    if(value.data!!.getValue("isProfessor") == false){
                        _userData.value.isProfessor = false
                    }
                }
            }
        }
    }

    fun addNewThesis(thesisName: String, thesisType: String,thesisDescription: String) {
        val temp = when(thesisType){
            "Tesi Compilativa" -> 0
            "Tesi Applicativa" -> 1
            else -> 2
        }
        val newThesis = getNewThesisEntry(thesisName, temp, thesisDescription)
        insertThesis(newThesis)
        onDialogConfirm()
    }

    private fun getNewThesisEntry(thesisName: String, thesisType: Int, thesisDescription: String): Thesis {
        return Thesis(
            title = thesisName,
            type = thesisType,
            description = thesisDescription
        )
    }

    private fun insertThesis(thesis: Thesis) {
        viewModelScope.launch {
            Firebase.firestore.collection("thesis").add(thesis)
        }
    }

    //Add Thesis Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    //Option Dialog
    private val _showOptionNameDialog = MutableStateFlow(false)
    val showOptionNameDialog: StateFlow<Boolean> = _showOptionNameDialog.asStateFlow()

    private val _showOptionEmailDialog = MutableStateFlow(false)
    val showOptionEmailDialog: StateFlow<Boolean> = _showOptionEmailDialog.asStateFlow()

    private val _showOptionWebDialog = MutableStateFlow(false)
    val showOptionWebDialog: StateFlow<Boolean> = _showOptionWebDialog.asStateFlow()


    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    private fun onDialogConfirm() {
        _showDialog.value = false
    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }

    fun onOptionDialogClicked(dialog: Int) {

        when(dialog){
            0 -> _showOptionNameDialog.value = true
            1 -> _showOptionEmailDialog.value = true
            2 -> _showOptionWebDialog.value = true
        }
    }

    fun onOptionDialogConfirm() {
        _showOptionNameDialog.value = false
        _showOptionEmailDialog.value = false
        _showOptionWebDialog.value = false
    }
}

