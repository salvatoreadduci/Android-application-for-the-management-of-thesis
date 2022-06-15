package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.model.ThesisViewModel

@Composable
fun AddThesisDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (String,String) -> Unit,
    viewModel: ThesisViewModel
){
    var nameInput by rememberSaveable { mutableStateOf("") }
    var descriptionInput by rememberSaveable { mutableStateOf("") }

    if (show) {
        AlertDialog(
            modifier = Modifier.fillMaxWidth(),
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onConfirm(nameInput,descriptionInput) } )
                { Text(text = "Add") }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = "Cancel") }
            },
            title = { Text(text = "Add a new Thesis") },
            text = {
                Column(modifier = Modifier.padding(8.dp) ) {
                    Text(text = "Name")
                    TextField(
                        value = nameInput,
                        onValueChange = {nameInput = it},
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.padding(16.dp))

                    Text(text = "Type")
                    TypeRadioList(viewModel)

                    Spacer(modifier = Modifier.padding(8.dp))

                    Text(text = "Description")
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