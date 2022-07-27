package com.whyskey.tesiunical.model

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.*
import coil.ImageLoader
import com.whyskey.tesiunical.data.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThesisViewModel(application: Application) : ViewModel(){

    //Database
    private val repository: ThesisRepository
    //val allThesis: LiveData<List<Thesis>>
    val allCompilation: LiveData<List<Thesis>>
    val allExperimental: LiveData<List<Thesis>>
    val corporateAmount: LiveData<Int>
    val erasmusAmount: LiveData<Int>
    val compilationAmount: LiveData<Int>
    val experimentalAmount: LiveData<Int>
    val totalAmount: LiveData<Int>

    val picProfile: LiveData<String>
    val nameAccount: LiveData<String>
    val emailAccount: LiveData<String>
    val webAccount: LiveData<String>

    val maxExperimental: LiveData<Int>
    val maxCompilation: LiveData<Int>
    val maxCorporate: LiveData<Int>
    val maxErasmus: LiveData<Int>
    val maxTotal: LiveData<Int>

    init {
        val thesisDb = ThesisRoomDatabase.getDatabase(application)
        val thesisDao = thesisDb.thesisDao()
        val userDao = thesisDb.userDao()
        repository = ThesisRepository(thesisDao,userDao)

        //allThesis = repository.readAllData
        allCompilation = repository.readAllCompilation
        allExperimental = repository.readAllExperimental
        corporateAmount = repository.corporateAmount
        erasmusAmount = repository.erasmusAmount
        compilationAmount = repository.compilationAmount
        experimentalAmount = repository.experimentalAmount
        totalAmount = repository.totalAmount

        picProfile = repository.pic
        nameAccount = repository.name
        emailAccount = repository.email
        webAccount = repository.webSite

        maxExperimental = repository.maxExperimental
        maxCompilation = repository.maxCompilation
        maxCorporate = repository.maxCorporate
        maxErasmus = repository.maxErasmus
        maxTotal = repository.maxTotal
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



    private var _type = Type.COMPILATION
    var type: Type = _type


    fun changeLimit(name: String, value: Int){
        setLimit(name,value)
    }

    private fun setLimit(name: String, value: Int){

        viewModelScope.launch {
            when(name){
               "Compilation Thesis" -> repository.setMaxCompilation(value)
                "Experimental  Thesis" -> repository.setMaxExperimental(value)
                "Corporate Thesis" -> repository.setMaxCorporate(value)
                "Erasmus Thesis" -> repository.setMaxErasmus(value)
                else -> null

            }
        }

    }

    fun changeName(name: String){
        setName(name)
        onOptionDialogConfirm()
    }

    private fun setName(name: String){
        viewModelScope.launch {
            repository.setName(name)
        }
    }

    fun changeEmail(email: String){
        setEmail(email)
        onOptionDialogConfirm()
    }

    private fun setEmail(email: String){
        viewModelScope.launch {
            repository.setEmail(email)
        }
    }

    fun changeWebSite(web: String){
        setWeb(web)
        onOptionDialogConfirm()
    }

    private fun setWeb(web: String){
        viewModelScope.launch {
            repository.setWeb(web)
        }
    }

    private fun insertThesis(thesis: Thesis) {
        viewModelScope.launch {
            repository.addThesis(thesis)
        }
    }

    private fun deleteThesis(thesis: Thesis) {
        viewModelScope.launch {
            repository.deleteThesis(thesis)
        }
    }

    private fun getNewThesisEntry(thesisName: String, thesisType: Type, thesisDescription: String): Thesis {
        var temp= 0
        when(thesisType){
            Type.COMPILATION -> temp = 4
            Type.EXPERIMENTAL -> temp = 5
            else -> null
        }

        return Thesis(
            name = thesisName,
            type = temp,
            description = thesisDescription
        )
    }

    fun addNewThesis(thesisName: String, thesisDescription: String) {
        val newThesis = getNewThesisEntry(thesisName, type, thesisDescription)
        insertThesis(newThesis)
        onDialogConfirm()
    }

    fun removeThesis(thesis: Thesis){
        deleteThesis(thesis)
    }



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

