package com.whyskey.tesiunical.data

import androidx.compose.runtime.Immutable

@Immutable
data class Thesis(
    val name: String,
    val description: String,
    val expanded: Boolean = false
)

object ThesisData{
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