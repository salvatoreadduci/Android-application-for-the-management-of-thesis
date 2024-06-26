package com.whyskey.tesiunical.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R


@Composable
fun <T> ThesisCard(
    title: String,
    onClickSeeAll: (String) -> Unit,
    data: List<T>,
    row: @Composable (T) -> Unit
){
    Card{
        Column {
            Column(Modifier.padding(16.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6)
            }

            Column(Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp)) {
                data.take(SHOWN_ITEMS).forEach { row(it) }
            }

            SeeAllButton(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = "All $title"
                },
                title = title,
                onClick = { onClickSeeAll(title) },
            )
        }
    }
}

@Composable
private fun SeeAllButton(
    modifier: Modifier = Modifier,
    title: String,
    onClick: (String) -> Unit
) {
    TextButton(
        onClick = { onClick(title) },
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(
            stringResource(R.string.see_all),
            style = MaterialTheme.typography.button
        )
    }
}

private const val SHOWN_ITEMS = 3