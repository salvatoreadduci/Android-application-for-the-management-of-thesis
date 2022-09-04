package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Session
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
            modifier = Modifier.height(180.dp),
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
                    Text(
                        text = "",
                        modifier = Modifier.height(4.dp)
                    )
                    OutlinedTextField(
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
fun ChangeLimitDialog(
    show: Boolean,
    title: String,
    onDismiss: () -> Unit,
    onConfirm: (String,String,String,String,String,String) -> Unit,
    item: Session,
    idList: String,
    viewModel: ThesisViewModel
){
    if(show){
        var inputApplicative by rememberSaveable { mutableStateOf(item.applicative["max"].toString()) }
        var inputCompilation by rememberSaveable { mutableStateOf(item.compilation["max"].toString()) }
        var inputCorporate by rememberSaveable { mutableStateOf(item.corporate["max"].toString()) }
        var inputErasmus by rememberSaveable { mutableStateOf(item.erasmus["max"].toString()) }
        var inputResearch by rememberSaveable { mutableStateOf(item.research["max"].toString()) }
        AlertDialog(
            onDismissRequest =  onDismiss,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(idList,inputApplicative,inputCompilation,inputCorporate,inputErasmus,inputResearch)
                } )
                { Text(text = stringResource(id = R.string.save)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = title)},
            text = {
                Text(
                    text = "",
                    modifier = Modifier.height(4.dp))
                Column(modifier = Modifier.padding(8.dp)){
                    LimitThesisRow(
                        title = stringResource(id = R.string.application_thesis),
                        value = inputApplicative,
                        idList = idList,
                        viewModel = viewModel,
                        onChangeValue = {inputApplicative = it}
                    )
                    LimitThesisRow(
                        title = stringResource(id = R.string.compilation_thesis),
                        value = inputCompilation,
                        idList = idList,
                        viewModel = viewModel,
                        onChangeValue = {inputCompilation = it}
                    )
                    LimitThesisRow(
                        title = stringResource(id = R.string.corporate_thesis),
                        value = inputCorporate,
                        idList = idList,
                        viewModel = viewModel,
                        onChangeValue = {inputCorporate = it}
                    )
                    LimitThesisRow(
                        title = stringResource(id = R.string.erasmus_thesis),
                        value = inputErasmus,
                        idList = idList,
                        viewModel = viewModel,
                        onChangeValue = {inputErasmus = it}
                    )
                    LimitThesisRow(
                        title = stringResource(id = R.string.research_thesis),
                        value = inputResearch,
                        idList = idList,
                        viewModel = viewModel,
                        onChangeValue = {inputResearch = it}
                    )
                }
            }
        )
    }
}

@Composable
fun LimitThesisRow(
    title: String,
    value: String,
    idList: String,
    onChangeValue: (String) -> Unit,
    viewModel: ThesisViewModel
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Row(
            modifier = Modifier
                .height(58.dp)
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))

            Text(text = title)
            Spacer(modifier = Modifier.weight(1f))
            OutlinedTextField(
                modifier = Modifier
                    .width(60.dp)
                    .height(50.dp),
                value = value,
                onValueChange = onChangeValue,
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
            )
        }
    }
    Divider()
}