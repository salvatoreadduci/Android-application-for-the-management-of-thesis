package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.model.ThesisViewModel
import kotlin.NumberFormatException

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SettingsRow(
    image: ImageVector,
    title: String,
    value: Any,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = onClick
    ){
        Row(
            modifier = Modifier
                .height(68.dp)
                .padding(start = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = image,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(text = title)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "$value")
            }

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Filled.Edit,
                contentDescription = "Edit"
            )
        }
    }
    Divider()
}