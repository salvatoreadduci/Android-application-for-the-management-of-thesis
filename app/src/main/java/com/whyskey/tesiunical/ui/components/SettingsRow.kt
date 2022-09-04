package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R

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

            if(value != "" || title ==  stringResource(id = R.string.web_site) || title ==  stringResource(id = R.string.password)){
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "$value",
                        style = MaterialTheme.typography.body2
                    )
                }
            } else {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6
                )
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