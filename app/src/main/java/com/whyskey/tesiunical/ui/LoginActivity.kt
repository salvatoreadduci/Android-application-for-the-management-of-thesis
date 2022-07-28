package com.whyskey.tesiunical.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.ThesisApp
import com.whyskey.tesiunical.model.ThesisViewModel

@Composable
fun LoginActivity(
    viewModel: ThesisViewModel
) {

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
    ) {
        val emailValue = remember { mutableStateOf("") }
        val passwordValue = remember { mutableStateOf("") }

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
            value =passwordValue.value,
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            onValueChange = {
                passwordValue.value = it
            }
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = {
                viewModel.auth.signInWithEmailAndPassword(emailValue.value,passwordValue.value)

            }
        ){
            Text(text = "Login")
        }


    }
}