package com.whyskey.tesiunical.ui

import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.model.UserState
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme
import java.lang.IllegalArgumentException

@Composable
fun Login(
    viewModel: ThesisViewModel,
    onRegister: () -> Unit,
    onLogin: () -> Unit,
) {
    TesiUnicalTheme {
        val vm = UserState.current

        Column(
            modifier = Modifier
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
                value = passwordValue.value,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                onValueChange = {
                    passwordValue.value = it
                }
            )
            val context = LocalContext.current
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                modifier = Modifier
                    .fillMaxWidth(),
                onClick = {
                    try{
                        viewModel.auth.signInWithEmailAndPassword(emailValue.value, passwordValue.value)
                            .addOnSuccessListener {
                                onLogin()
                            }.addOnFailureListener {
                                Toast.makeText(context, "Login fallito, dati non inseriti correttamente",
                                    Toast.LENGTH_SHORT).show()
                            }
                    } catch (e: Exception){
                        Toast.makeText(context, "Login fallito, dati non inseriti correttamente",
                            Toast.LENGTH_SHORT).show()
                    }

                }
            ) {
                Text(text = stringResource(id = R.string.login))
            }
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = stringResource(id = R.string.are_you_registered),
                modifier = Modifier.clickable { onRegister() }
            )
        }
    }
}