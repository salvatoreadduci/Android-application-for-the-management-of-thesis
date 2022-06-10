package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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

/*object ThesisData{
    val comparative: List<Thesis> = listOf(
        Thesis(
            name = "Tesi A",
            description = "Lorem ipsum"
        ),
        Thesis(
            name = "Tesi B",
            description = "Lorem ipsum"
        ),
        Thesis(
            name = "Tesi C",
            description = "Lorem ipsum"
        ),
        Thesis(
            name = "Tesi D",
            description = "Lorem ipsum"
        )
    )

    val applicative: List<Thesis> = listOf(
        Thesis(
            name = "Tesi X",
            description = "Lorem ipsum"
        ),
        Thesis(
            name = "Tesi Y",
            description = "Lorem ipsum"
        ),
        Thesis(
            name = "Tesi Z",
            description = "Lorem ipsum"
        ),
        Thesis(
            name = "Tesi W",
            description = "Lorem ipsum"
        )
    )


}

 */