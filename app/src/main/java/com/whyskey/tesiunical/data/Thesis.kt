package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type{
    DEPARTMENTAL,
    CORPORATE,
    ERASMUS,
    COMPILATION,
    EXPERIMENTAL
}

class DataType(
    val name: Type,
    var amount: Any,
    var max: Int
)

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