package com.whyskey.tesiunical.data

import androidx.lifecycle.LiveData

class ThesisRepository(
    private val thesisDao: ThesisDao,
    private val accountDao: AccountDao
    ) {

    //val readAllData: LiveData<List<Thesis>> = thesisDao.getThesis()
    val readAllCompilation: LiveData<List<Thesis>> = thesisDao.getCompilationThesis()
    val readAllExperimental: LiveData<List<Thesis>> = thesisDao.getExperimentalThesis()
    val corporateAmount: LiveData<Int> = thesisDao.getAmount(2)
    val erasmusAmount: LiveData<Int> = thesisDao.getAmount(3)
    val compilationAmount: LiveData<Int> = thesisDao.getAmount(4)
    val experimentalAmount: LiveData<Int> = thesisDao.getAmount(5)
    val totalAmount: LiveData<Int> = thesisDao.getTotal()

    val pic: LiveData<String> = accountDao.getPic()
    val name: LiveData<String> = accountDao.getName()
    val email: LiveData<String> = accountDao.getEmail()
    val webSite: LiveData<String> = accountDao.getWebSite()

    val maxExperimental: LiveData<Int> = accountDao.getMaxExperimental()
    val maxCompilation: LiveData<Int> = accountDao.getMaxCompilation()
    val maxCorporate: LiveData<Int> = accountDao.getMaxCorporate()
    val maxErasmus: LiveData<Int> = accountDao.getMaxErasmus()
    val maxTotal: LiveData<Int> = accountDao.getMaxTotal()


    suspend fun addThesis(thesis: Thesis){
        thesisDao.insert(thesis)
    }
    suspend fun updateThesis(thesis: Thesis){
        thesisDao.update(thesis)
    }
    suspend fun deleteThesis(thesis: Thesis){
        thesisDao.delete(thesis)
    }

    suspend fun setName(name: String){
        accountDao.setName(name)
    }

    suspend fun setEmail(email: String){
        accountDao.setEmail(email)
    }

    suspend fun setWeb(web: String){
        accountDao.setWeb(web)
    }

    suspend fun setMaxCompilation(value: Int){
        accountDao.setMaxCompilation(value)
    }

    suspend fun setMaxExperimental(value: Int){
        accountDao.setMaxExperimental(value)
    }

    suspend fun setMaxCorporate(value: Int){
        accountDao.setMaxCorporate(value)
    }

    suspend fun setMaxErasmus(value: Int){
        accountDao.setMaxErasmus(value)
    }
}
