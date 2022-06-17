package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

enum class Type(
    val color: Color,
    var amount: Int,
    var max: Int,
){
    DEPARTMENTAL(
        color = Color(0xFF004940),
        amount = 19,
        max = 20
    ),
    CORPORATE(
        color = Color(0xFFFFDC78),
        amount = 15,
        max = 30
    ),
    ERASMUS(
        color = Color(0xFFFF6951),
        amount = 3,
        max = 15
    ),
    COMPILATION(
        color = Color(0xFF004940),
        amount = 8,
        max = -1
    ),
    EXPERIMENTAL(
        color = Color(0xFF004940),
        amount = 11,
        max = -1
    )
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