package com.whyskey.tesiunical.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

@Composable
fun RegisterActivity(
    viewModel: ThesisViewModel
) {
    TesiUnicalTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val nameValue = remember { mutableStateOf(TextFieldValue()) }
            val emailValue = remember { mutableStateOf(TextFieldValue()) }
            val passwordValue = remember { mutableStateOf(TextFieldValue()) }

            OutlinedTextField(
                label = { Text(text = stringResource(id = R.string.name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = nameValue.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    emailValue.value = it
                }
            )

            OutlinedTextField(
                label = { Text(text = "Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                value = emailValue.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    emailValue.value = it
                }
            )

            OutlinedTextField(
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = passwordValue.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    passwordValue.value = it
                }
            )

            val checkedState = remember { mutableStateOf(true) }
            Row(){
                Text(
                    text = stringResource(id = R.string.are_you_a_professor),
                    modifier = Modifier.padding(top = 12.dp)
                )
                Checkbox(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
            }
           
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    viewModel.auth.createUserWithEmailAndPassword(
                        emailValue.value.text.trim(),
                        passwordValue.value.text.trim()
                        //vm.singIn()
                        //viewModel.registerAccount()
                    )
                }
            ) {
                Text(text = "Register")
            }
        }
    }
}