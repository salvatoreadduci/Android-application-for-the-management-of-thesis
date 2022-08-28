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
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel
import kotlin.reflect.KFunction3

@Composable
fun AddThesisDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: KFunction3<String, String, String, Unit>,
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
                    onConfirm(nameInput,selectedOption,descriptionInput)
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