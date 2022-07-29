package com.whyskey.tesiunical.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.whyskey.tesiunical.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThesisViewModel() : ViewModel(){

    private var _auth: FirebaseAuth = Firebase.auth
    val auth = _auth

    private var _storage = Firebase.storage
    private val storage = _storage
    private var storageRef = storage.reference

    private val user = auth.currentUser

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

    private val queryCompilation = Firebase.firestore.collection("thesis").whereEqualTo("type",0)
    private val queryApplication= Firebase.firestore.collection("thesis").whereEqualTo("type",1)
    private val queryResearch = Firebase.firestore.collection("thesis").whereEqualTo("type",2)
    private val queryCorporate= Firebase.firestore.collection("thesis").whereEqualTo("type",3)
    private val queryErasmus = Firebase.firestore.collection("thesis").whereEqualTo("type",4)
    private val queryUser = Firebase.firestore.collection("account").document(user!!.uid)

    init {
        getCompilationThesis()
        getApplicationThesis()
        getResearchThesis()
        getCorporateThesis()
        getErasmusThesis()
        getUserData()
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

    private fun getCompilationThesis(){
        viewModelScope.launch {
            queryCompilation.addSnapshotListener { value, _ ->
                if(value != null) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents
                    documents.forEach { it ->
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            thesis.add(temp)
                        }
                    }
                    _compilationThesis.value = thesis
                }
            }
        }
    }

    private fun getApplicationThesis(){
        viewModelScope.launch {
            queryApplication.addSnapshotListener { value, _ ->

                if(value != null) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents
                    documents.forEach { it ->
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            thesis.add(temp)
                        }
                    }
                    _applicationThesis.value = thesis
                }
            }
        }
    }

    private fun getResearchThesis(){
        viewModelScope.launch {
            queryResearch.addSnapshotListener { value, _ ->
                if(value != null) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents
                    documents.forEach { it ->
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            thesis.add(temp)
                        }
                    }
                    _researchThesis.value = thesis
                }
            }
        }
    }

    private fun getCorporateThesis(){
        viewModelScope.launch {
            queryCorporate.addSnapshotListener { value, _ ->
                if(value != null) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents
                    documents.forEach { it ->
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            thesis.add(temp)
                        }
                    }
                    _corporateThesis.value = thesis
                }
            }
        }
    }

    private fun getErasmusThesis(){
        viewModelScope.launch {
            queryErasmus.addSnapshotListener { value, _ ->
                if(value != null) {
                    val thesis = ArrayList<Thesis>()
                    val documents = value.documents
                    documents.forEach { it ->
                        val temp = it.toObject(Thesis::class.java)
                        if(temp != null){
                            temp.id = it.id
                            thesis.add(temp)
                        }
                    }
                    _erasmusThesis.value = thesis
                }
            }
        }
    }

    private fun getUserData(){
        viewModelScope.launch {
            queryUser.addSnapshotListener { value, _ ->
                if(value != null) {
                    _userData.value = value.toObject()!!
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

