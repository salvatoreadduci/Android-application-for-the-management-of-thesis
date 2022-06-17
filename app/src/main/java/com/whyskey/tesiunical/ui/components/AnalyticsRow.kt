package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.unit.dp

@Composable
fun AnalyticsRow(
    modifier: Modifier = Modifier,
    color: Color,
    title: String,
    amount: Int,
    max: Int
) {

    Row(
        modifier = modifier
            .height(68.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val typography = MaterialTheme.typography
        AccountIndicator(
            color = color,
            modifier = Modifier
        )
        Spacer(Modifier.width(12.dp))
        Text(text = title, style = typography.body1)
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

@Composable
private fun AccountIndicator(color: Color, modifier: Modifier = Modifier) {
    Spacer(
        modifier
            .size(4.dp, 36.dp)
            .background(color = color)
    )
}