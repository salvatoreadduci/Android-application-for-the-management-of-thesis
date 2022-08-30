package com.whyskey.tesiunical.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Account
import com.whyskey.tesiunical.model.ThesisViewModel
import kotlin.reflect.KFunction3
import kotlin.reflect.KFunction4
import kotlin.reflect.KFunction7

@Composable
fun AddThesisDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: KFunction4<String, String, String, String, Unit>,
    viewModel: ThesisViewModel
){
    var nameInput by rememberSaveable { mutableStateOf("") }
    var descriptionInput by rememberSaveable { mutableStateOf("") }
    val radioOptions = listOf(
        stringResource(id = R.string.compilation_thesis),
        stringResource(id = R.string.application_thesis),
        stringResource(id = R.string.research_thesis)
    )
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }


    if (show) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(nameInput,selectedOption,descriptionInput,viewModel.userData.value.id)
                    selectedOption = radioOptions[0]
                    nameInput = ""
                    descriptionInput =""
                })
                { Text(text = stringResource(id = R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = stringResource(id = R.string.thesis_dialog)) },
            text = {
                Column(modifier = Modifier.padding(8.dp) ) {
                    Text(text = stringResource(id = R.string.title))
                    TextField(
                        value = nameInput,
                        onValueChange = {nameInput = it},
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(stringResource(id = R.string.type))

                    Column(Modifier.selectableGroup()) {
                        radioOptions.forEach { text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = { onOptionSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.body1.merge(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(text = stringResource(id = R.string.description))
                    TextField(
                        modifier = Modifier.height(180.dp),
                        value = descriptionInput,
                        onValueChange = {descriptionInput = it},
                    )

                }
            }

        )
    }
}

@Composable
fun CustomThesisDialog(
    show: Boolean,
    profile: Account,
    onConfirm: KFunction7<String, String, String, String, Int, String, String, Unit>,
    onDismiss:() -> Unit,
    viewModel: ThesisViewModel
    ){
    viewModel.returnThesis(viewModel.userData.value.id)
    var nameInput by rememberSaveable { mutableStateOf("") }
    var supervisorInput by rememberSaveable { mutableStateOf("") }
    val radioOptions = listOf(
        stringResource(id = R.string.corporate_thesis),
        stringResource(id = R.string.erasmus_thesis),
    )

    val sessionOptions = listOf(
        stringResource(id = R.string.march_session),
        stringResource(id = R.string.july_session),
        stringResource(id = R.string.september_session),
        stringResource(id = R.string.december_session)
    )
    var (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    var (selectedSession, onSessionSelected) = remember { mutableStateOf(sessionOptions[0]) }
    Log.d("TAG",viewModel.thesis.value.size.toString())
    if (show) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    Log.d("TAG",viewModel.thesis.value.size.toString())
                    if(!viewModel.userData.value.isProfessor && (!viewModel.userData.value.hasThesis
                                && viewModel.thesis.value.size < 3 && viewModel.thesis.value.find { thesis -> thesis.id_professor != profile.id } == null)) {
                        val temp = when (selectedSession) {
                            "Sessione di Marzo" -> 0
                            "Sessione di Luglio" -> 1
                            "Sessione di Settembre" -> 2
                            else -> 3
                        }
                        viewModel.addNewCustomRequest(
                            nameInput,
                            supervisorInput,
                            selectedOption,
                            temp
                        )
                        //onConfirm( viewModel.userData.value.id, profile.id,"",viewModel.userData.value.name, temp, nameInput,viewModel.userData.value.email)
                        selectedOption = radioOptions[0]
                        selectedSession = sessionOptions[0]
                        nameInput = ""
                        supervisorInput = ""
                    }
                })
                { Text(text = stringResource(id = R.string.send)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = stringResource(id = R.string.custom_thesis_dialog)) },
            text = {
                Column(
                    modifier = Modifier.padding(8.dp) ) {
                    Text(text = stringResource(id = R.string.title))
                    TextField(
                        value = nameInput,
                        onValueChange = {nameInput = it},
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(text = stringResource(id = R.string.supervisor))
                    TextField(
                        value = supervisorInput,
                        onValueChange = {supervisorInput = it},
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(stringResource(id = R.string.type))

                    Column(Modifier.selectableGroup()) {
                        radioOptions.forEach { text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (text == selectedOption),
                                        onClick = { onOptionSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (text == selectedOption),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.body1.merge(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(stringResource(id = R.string.session))

                    Column(Modifier.selectableGroup()) {
                        sessionOptions.forEach { text ->
                            Row(
                                Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .selectable(
                                        selected = (text == selectedSession),
                                        onClick = { onSessionSelected(text) },
                                        role = Role.RadioButton
                                    )
                                    .padding(horizontal = 16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (text == selectedSession),
                                    onClick = null
                                )
                                Text(
                                    text = text,
                                    style = MaterialTheme.typography.body1.merge(),
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            }

        )
    }
}