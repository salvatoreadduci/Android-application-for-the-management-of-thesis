package com.whyskey.tesiunical.data

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class ThesisRepository(private val thesisDao: ThesisDao) {

    val readAllData: LiveData<List<Thesis>> = thesisDao.getThesis()

    suspend fun addThesis(thesis: Thesis){
        thesisDao.insert(thesis)
    }
    suspend fun updateThesis(thesis: Thesis){
        thesisDao.update(thesis)
    }
    suspend fun deleteThesis(thesis: Thesis){
        thesisDao.delete(thesis)
    }

}