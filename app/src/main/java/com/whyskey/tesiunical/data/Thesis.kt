package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type(){
    DEPARTMENTAL,
    CORPORATE,
    ERASMUS,
    COMPILATION,
    EXPERIMENTAL
}

@Immutable
@Entity(tableName = "thesis")
data class Thesis(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "type")
    val type: Int,
    @ColumnInfo(name = "description")
    val description: String
)