package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.model.ThesisViewModel

@Composable
fun AnalyticsRow(
    viewModel: ThesisViewModel,
    modifier: Modifier = Modifier,
    type: String,
    amount: Int?,
    max: Int?
) {
    Row(
        modifier = modifier
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(12.dp))
        Text(text = type, style = MaterialTheme.typography.subtitle1)
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            if(viewModel.userData.value.hasLimit){
                Text(
                    text = "$amount/$max",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            } else {
                Text(
                    text = "$amount",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )
            }

        }
        Spacer(Modifier.width(16.dp))
    }
}