package com.whyskey.tesiunical.model

import android.app.Application
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.whyskey.tesiunical.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThesisViewModel(application: Application) : ViewModel(){


    private var _auth: FirebaseAuth = Firebase.auth
    val auth = _auth
    private val user = auth.currentUser

    private val _compilationThesis = mutableStateOf<List<Thesis>>(emptyList())
    val compilationThesis: State<List<Thesis>>
        get() = _compilationThesis

    private val _applicationThesis = mutableStateOf<List<Thesis>>(emptyList())
    val applicationThesis: State<List<Thesis>>
        get() = _applicationThesis

    private val _userData = mutableStateOf<Account>(Account())
    val userData: State<Account>
        get() = _userData

    private val queryCompilation = Firebase.firestore.collection("thesis").whereEqualTo("type",0)
    private val queryApplication= Firebase.firestore.collection("thesis").whereEqualTo("type",1)
    private val queryUser = Firebase.firestore.collection("account").document(user!!.uid)

    init {
        getCompilationThesis()
        getApplicationThesis()
        getUserData()
    }

    private fun getCompilationThesis(){
        viewModelScope.launch {
            queryCompilation.addSnapshotListener { value, _ ->
                if(value != null) {
                    _compilationThesis.value = value.toObjects()
                }
            }
        }
    }

    private fun getApplicationThesis(){
        viewModelScope.launch {
            queryApplication.addSnapshotListener { value, _ ->
                if(value != null) {
                    _applicationThesis.value = value.toObjects()
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

    fun addNewThesis(thesisName: String, thesisType: Int,thesisDescription: String) {
        val newThesis = getNewThesisEntry(thesisName, thesisType, thesisDescription)
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
            else -> null
        }
    }

    fun onOptionDialogConfirm() {
        _showOptionNameDialog.value = false
        _showOptionEmailDialog.value = false
        _showOptionWebDialog.value = false
    }
}

