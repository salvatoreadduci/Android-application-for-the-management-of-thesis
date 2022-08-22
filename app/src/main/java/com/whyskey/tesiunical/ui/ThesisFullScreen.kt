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
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.ThesisRow

@Composable
fun ThesisFullScreen(
    list: List<Thesis>,
    viewModel: ThesisViewModel,
    title: String
) {
    var expandedThesis by remember { mutableStateOf<String?>(null) }

    Card{
        Column {
            Column(Modifier.padding(16.dp)) {
                Text(text = title)
            }

            LazyColumn(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 32.dp),
            ){
                items(list) { thesis ->
                    ThesisRow(
                        viewModel = viewModel,
                        profile = viewModel.userData.value,
                        name = thesis.title,
                        description = thesis.description,
                        expanded = expandedThesis == thesis.title,
                        onClick = {
                            expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
                        },
                        onDelete = { viewModel.removeThesis(thesis.id) },
                        onRequest = { }
                    )
                }
            }
        }
    }
}