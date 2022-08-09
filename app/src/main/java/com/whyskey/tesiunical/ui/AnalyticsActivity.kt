package com.whyskey.tesiunical.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.AnalyticsRow

@Composable
fun Analytics(
    viewModel: ThesisViewModel
) {
    val list: List<Map<String,List<Int>>> = listOf(
        viewModel.userData.value.march_session,
        viewModel.userData.value.july_session,
        viewModel.userData.value.september_session,
        viewModel.userData.value.december_session,
    )

    var count = 0
    var title = ""

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        list.forEach { it ->
            when (count) {
                0 -> title = stringResource(id = R.string.march_session)
                1 -> title = stringResource(id = R.string.july_session)
                2 -> title = stringResource(id = R.string.september_session)
                3 -> title = stringResource(id = R.string.december_session)
            }

            count++
            AnalyticsBody(
                title = title,
                items = it.toList(),
                rows = {
                    AnalyticsRow(type = it.first, amount = it.second[0], max = it.second[1])
                }
            )
        }
    }

}

@Composable
fun <T> AnalyticsBody(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    rows: @Composable (T) -> Unit
){
        Spacer(modifier.height(10.dp))
        Card {
            Text(text = title)
            Column(modifier = modifier.padding(12.dp)) {
                items.forEach { item ->
                    rows(item)
                }
            }
        }

}