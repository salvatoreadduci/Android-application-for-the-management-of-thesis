package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel

@Composable
fun ChangeOptionDialog(
    show: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
    viewModel: ThesisViewModel
){

    var input by rememberSaveable { mutableStateOf("") }

    if(show){
        AlertDialog(
            onDismissRequest =  onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(input)
                    input = ""
                } )
                { Text(text = stringResource(id = R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = title)},
            text = {
                Column(modifier = Modifier.padding(8.dp)){
                    Text(text = "")
                    TextField(
                        value = input,
                        onValueChange = {input = it},
                        maxLines = 1
                    )
                }
            }
        )
    }
}

@Composable
fun <T> ChangeLimitDialog(
    show: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    items: List<T>,
    rows: @Composable (T) -> Unit,
    viewModel: ThesisViewModel
){
    if(show){
        AlertDialog(
            onDismissRequest =  onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm()
                } )
                { Text(text = stringResource(id = R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = title)},
            text = {
                Column(modifier = Modifier.padding(8.dp)){
                   items.forEach {
                       rows(it)
                   }
                }
            }
        )
    }
}