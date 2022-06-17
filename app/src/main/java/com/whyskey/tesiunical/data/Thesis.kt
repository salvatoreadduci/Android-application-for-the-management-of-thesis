package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type(){
    DEPARTMENTAL,
    CORPORATE,
    ERASMUS,
    COMPILATION,
    EXPERIMENTAL,
}

val colors = listOf<Color>(Color(0xFF004940),Color(0xFFFFDC78),Color(0xFFFF6951))

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