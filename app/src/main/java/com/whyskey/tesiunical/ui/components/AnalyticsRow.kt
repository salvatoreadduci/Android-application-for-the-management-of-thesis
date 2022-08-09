package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp

@Composable
fun AnalyticsRow(
    modifier: Modifier = Modifier,
    type: String,
    amount: Any,
    max: Any
) {
    Row(
        modifier = modifier
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography
        Spacer(Modifier.width(12.dp))
        Text(text = type, style = typography.body1)
        Spacer(Modifier.weight(1f))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "$amount/$max",
                style = typography.h6,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
        Spacer(Modifier.width(16.dp))
    }
}