package com.whyskey.tesiunical.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ThesisDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(thesis: Thesis)

    @Update
    suspend fun update(thesis: Thesis)

    @Delete
    suspend fun delete(thesis: Thesis)

    @Query("SELECT * from thesis ORDER BY name ASC")
    fun getThesis(): LiveData<List<Thesis>>

    @Query("SELECT * from thesis WHERE type=4 ORDER BY name ASC")
    fun getCompilationThesis(): LiveData<List<Thesis>>

    @Query("SELECT * from thesis WHERE type=5 ORDER BY name ASC")
    fun getExperimentalThesis(): LiveData<List<Thesis>>

}