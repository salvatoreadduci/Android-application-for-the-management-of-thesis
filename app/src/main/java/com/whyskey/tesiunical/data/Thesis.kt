package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type{
    CORPORATE,
    ERASMUS,
    COMPILATION,
    EXPERIMENTAL
}

class DataType(
    val name: Type,
    var amount: Any,
    var max: Any
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