package com.whyskey.tesiunical.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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

    var show by rememberSaveable { mutableStateOf(false) }
    var thesisId by rememberSaveable { mutableStateOf("") }
    var thesisTitle by rememberSaveable { mutableStateOf("") }
    var thesisType by rememberSaveable { mutableStateOf(0) }
    var showSession by rememberSaveable { mutableStateOf(false) }
    var session by rememberSaveable { mutableStateOf("") }

    val id =if(list.isNotEmpty()){
        list[0].id_professor
    } else {
        ""
    }

    ConfirmDeleteDialog(
        show = show,
        onDismiss = { show = false },
        onRemove = { viewModel.removeThesis(thesisId)
            show = false
        }
    )

    Card(modifier = Modifier.fillMaxWidth()){
        Column {
            Column(Modifier.padding(16.dp)) {
                Text(text = title)
            }

            LazyColumn(
                Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp)
            ){
                items(list) { thesis ->
                    ThesisRow(
                        viewModel = viewModel,
                        profileId = id,
                        name = thesis.title,
                        description = thesis.description,
                        expanded = expandedThesis == thesis.title,
                        onClick = {
                            expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
                        },
                        onDelete = { show = true
                            thesisId = thesis.id },
                        onRequest = {
                            thesisId = thesis.id
                            thesisType = thesis.type
                            thesisTitle = thesis.title
                            showSession = true
                        }
                    )

                    SessionDialog(
                        show = showSession,
                        selectedOption = session,
                        onOptionSelected = {session = it},
                        onDismiss = {
                            if(session != ""){
                                showSession = false
                            }
                        },
                        onRequest = {
                            val temp = when(session){
                                "Sessione di Marzo" -> 0
                                "Sessione di Luglio" -> 1
                                "Sessione di Settembre" -> 2
                                else -> 3
                            }
                            viewModel.addNewRequest(
                                viewModel.userData.value.id, id,thesisId,viewModel.userData.value.name, temp, thesisType, thesisTitle,viewModel.userData.value.email
                            )
                            showSession = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}