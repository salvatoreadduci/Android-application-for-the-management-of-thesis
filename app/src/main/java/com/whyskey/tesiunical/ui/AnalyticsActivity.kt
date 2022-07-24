package com.whyskey.tesiunical.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.DataType
import com.whyskey.tesiunical.data.Type
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.AnalyticsRow

@Composable
fun Analytics(
    viewModel: ThesisViewModel
) {

    val experimental by viewModel.experimentalAmount.observeAsState(Int)
    val corporate by viewModel.corporateAmount.observeAsState(Int)
    val erasmus by viewModel.erasmusAmount.observeAsState(Int)
    val compilation by viewModel.compilationAmount.observeAsState(Int)
    val total by viewModel.totalAmount.observeAsState(Int)

    val typeList: List<DataType> = listOf(
        DataType(Type.COMPILATION, compilation, 28),
        DataType(Type.EXPERIMENTAL,experimental,15),
        DataType(Type.CORPORATE,corporate,20),
        DataType(Type.ERASMUS,erasmus,30),
    )

    AnalyticsBody(
        items = typeList,
        amountsTotal = total,
        maxTotal = typeList.sumOf { it.max },
        label = stringResource(id = R.string.total),
        rows = {
            AnalyticsRow(title = it.name.toString(), amount = it.amount, max = it.max)
        }
    )
}

@Composable
fun <T> AnalyticsBody(
    modifier: Modifier = Modifier,
    items: List<T>,
    amountsTotal: Any,
    maxTotal: Int,
    label: String,
    rows: @Composable (T) -> Unit
){
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {

        Column(modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(
                text = label,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Text(
                text = "$amountsTotal/$maxTotal",
                style = MaterialTheme.typography.h2,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }

        Spacer(Modifier.height(10.dp))
        Card {
            Column(modifier = Modifier.padding(12.dp)) {
                items.forEach { item ->
                    rows(item)
                }
            }
        }
    }
}