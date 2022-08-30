package com.whyskey.tesiunical.ui

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.model.UserState
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme
import kotlin.IllegalArgumentException

@Composable
fun RegisterActivity(
    viewModel: ThesisViewModel,
    onLogin: () -> Unit,
) {
    TesiUnicalTheme {
        val vm = UserState.current
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            val nameValue = remember { mutableStateOf(TextFieldValue()) }
            val emailValue = remember { mutableStateOf(TextFieldValue()) }
            val passwordValue = remember { mutableStateOf(TextFieldValue()) }
            val confirmPasswordValue = remember { mutableStateOf(TextFieldValue()) }

            OutlinedTextField(
                label = { Text(text = stringResource(id = R.string.name)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                value = nameValue.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    nameValue.value = it
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

            OutlinedTextField(
                label = { Text(text = "Conferma Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                value = confirmPasswordValue.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    confirmPasswordValue.value = it
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
            val context = LocalContext.current
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    try{
                        if(passwordValue == confirmPasswordValue){
                            viewModel.auth.createUserWithEmailAndPassword(
                                emailValue.value.text.trim(),
                                passwordValue.value.text.trim()
                            ).addOnSuccessListener {
                                viewModel.addNewAccount(nameValue.value.text, emailValue.value.text,checkedState.value)
                                vm.signIn()
                            }
                        } else{
                            Toast.makeText(context, "Password non inserita correttamente",
                                Toast.LENGTH_SHORT).show()
                        }

                    } catch (e: IllegalArgumentException){
                        Toast.makeText(context, "Registrazione fallita, dati non inseriti correttamente",
                            Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.register))
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = stringResource(id = R.string.are_you_registered_already),
                modifier = Modifier.clickable { onLogin() }
            )
        }
    }
}