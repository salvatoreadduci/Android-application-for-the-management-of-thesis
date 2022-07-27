package com.whyskey.tesiunical.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: Account)

    @Query("SELECT pic FROM user")
    fun getPic(): LiveData<String>

    @Query("SELECT name FROM user")
    fun getName(): LiveData<String>

    @Query("SELECT email FROM user")
    fun getEmail(): LiveData<String>

    @Query("SELECT site FROM user")
    fun getWebSite(): LiveData<String>

    @Query("UPDATE user SET name = :name")
    suspend fun setName(name: String)

    @Query("UPDATE user SET email = :email")
    suspend fun setEmail(email: String)

    @Query("UPDATE user SET site = :web")
    suspend fun setWeb(web: String)

    @Query("SELECT max_compilation FROM user")
    fun getMaxCompilation(): LiveData<Int>

    @Query("SELECT max_experimental FROM user")
    fun getMaxExperimental(): LiveData<Int>

    @Query("SELECT max_corporate FROM user")
    fun getMaxCorporate(): LiveData<Int>

    @Query("SELECT max_erasmus FROM user")
    fun getMaxErasmus(): LiveData<Int>

    @Query("SELECT SUM(max_erasmus + max_corporate + max_experimental + max_compilation) FROM user")
    fun getMaxTotal(): LiveData<Int>

    @Query("UPDATE user SET max_compilation = :value")
    suspend fun setMaxCompilation(value: Int)

    @Query("UPDATE user SET max_experimental = :value")
    suspend fun setMaxExperimental(value: Int)

    @Query("UPDATE user SET max_corporate = :value")
    suspend fun setMaxCorporate(value: Int)

    @Query("UPDATE user SET max_erasmus = :value")
    suspend fun setMaxErasmus(value: Int)

}