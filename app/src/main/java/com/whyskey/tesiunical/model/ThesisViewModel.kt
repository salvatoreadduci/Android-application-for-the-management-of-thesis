package com.whyskey.tesiunical.model

import android.app.Application
import androidx.lifecycle.*
import com.whyskey.tesiunical.ThesisApplication
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.data.ThesisDao
import com.whyskey.tesiunical.data.ThesisRepository
import com.whyskey.tesiunical.data.ThesisRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ThesisViewModel(application: Application) : ViewModel(){

    private val repository: ThesisRepository
    val allThesis: LiveData<List<Thesis>>

    init {
        val thesisDb = ThesisRoomDatabase.getDatabase(application)
        val thesisDao = thesisDb.thesisDao()
        repository = ThesisRepository(thesisDao)

        allThesis = repository.readAllData
    }

    private fun insertThesis(thesis: Thesis) {
        viewModelScope.launch {
            repository.addThesis(thesis)
        }
    }

    private fun getNewThesisEntry(thesisName: String, thesisType: Int, thesisDescription: String): Thesis {
        return Thesis(
            name = thesisName,
            type = thesisType,
            description = thesisDescription
        )
    }

    fun addNewThesis(thesisName: String, thesisType: Int, thesisDescription: String) {
        val newThesis = getNewThesisEntry(thesisName, thesisType, thesisDescription)
        insertThesis(newThesis)
    }

}