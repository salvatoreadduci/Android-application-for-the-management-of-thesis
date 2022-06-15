package com.whyskey.tesiunical.model

import android.app.Application
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.*
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.data.ThesisRepository
import com.whyskey.tesiunical.data.ThesisRoomDatabase
import com.whyskey.tesiunical.data.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ThesisViewModel(application: Application) : ViewModel(){

    //Add Thesis Dialog
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()

    private var _type = Type.COMPILATION
    var type: Type = _type

    //Database
    private val repository: ThesisRepository
    val allThesis: LiveData<List<Thesis>>
    val allCompilation: LiveData<List<Thesis>>
    val allExperimental: LiveData<List<Thesis>>

    init {
        val thesisDb = ThesisRoomDatabase.getDatabase(application)
        val thesisDao = thesisDb.thesisDao()
        repository = ThesisRepository(thesisDao)

        allThesis = repository.readAllData
        allCompilation = repository.readAllCompilation
        allExperimental = repository.readAllExperimental
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
        }

        return Thesis(
            name = thesisName,
            type = temp,
            description = thesisDescription
        )
    }

    fun  addNewThesis(thesisName: String, thesisDescription: String) {

        val newThesis = getNewThesisEntry(thesisName, type, thesisDescription)
        insertThesis(newThesis)
        onDialogConfirm()

    }

    fun removeThesis(thesis: Thesis){
        deleteThesis(thesis)
    }

    //Add Thesis Dialog

    fun onOpenDialogClicked() {
        _showDialog.value = true
    }

    private fun onDialogConfirm() {
        _showDialog.value = false

    }

    fun onDialogDismiss() {
        _showDialog.value = false
    }
}