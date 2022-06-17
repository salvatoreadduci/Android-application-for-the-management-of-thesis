package com.whyskey.tesiunical.ui.components

import android.util.Log
import android.util.Log.VERBOSE
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Type
import com.whyskey.tesiunical.model.ThesisViewModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun TypeRadioList(viewModel: ThesisViewModel) {

    var state by remember { mutableStateOf(true) }

    Column(Modifier.selectableGroup()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = state,
                onClick = {
                    state = true
                    viewModel.type = Type.COMPILATION
                },
                role = Role.RadioButton
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = state,
                onClick = null
            )
            Text(text = stringResource(id = R.string.compilation_thesis),
                Modifier.padding(start = 8.dp)
            )
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .selectable(
                selected = !state,
                onClick = {
                    state = false
                    viewModel.type = Type.EXPERIMENTAL
                },
                role = Role.RadioButton
            ),
            verticalAlignment = Alignment.CenterVertically
        ){
            RadioButton(
                selected = !state,
                onClick = null
            )
            Text(text = stringResource(id = R.string.experimental_thesis),
                Modifier.padding(start = 8.dp)
            )
        }

    }
}