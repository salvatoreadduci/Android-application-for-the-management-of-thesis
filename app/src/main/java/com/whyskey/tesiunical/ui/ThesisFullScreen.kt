package com.whyskey.tesiunical.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.data.ThesisData
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

@Composable
fun ThesisFullScreen() {
    var expandedThesis by remember { mutableStateOf<String?>(null) }

    Card{
        Column {
            Column(Modifier.padding(16.dp)) {
                Text(text = "PROVA")
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            ){
                items(ThesisData.applicative) { thesis ->
                    ThesisRow(
                        name = thesis.name,
                        expanded = expandedThesis == thesis.name,
                        onClick = { expandedThesis = if (expandedThesis == thesis.name) null else thesis.name }
                    )

                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    TesiUnicalTheme {
        ThesisFullScreen()
    }
}