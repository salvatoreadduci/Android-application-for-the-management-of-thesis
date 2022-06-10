package com.whyskey.tesiunical.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

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

}