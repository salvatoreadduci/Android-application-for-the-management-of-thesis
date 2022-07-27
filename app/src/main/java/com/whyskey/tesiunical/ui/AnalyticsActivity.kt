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

    val typeList: List<DataType> = listOf(
        DataType(Type.COMPILATION, viewModel.compilationAmount.observeAsState(Int).value, viewModel.maxCompilation.observeAsState(Int).value),
        DataType(Type.EXPERIMENTAL, viewModel.experimentalAmount.observeAsState(Int).value,viewModel.maxExperimental.observeAsState(Int).value),
        DataType(Type.CORPORATE, viewModel.corporateAmount.observeAsState(Int).value,viewModel.maxCorporate.observeAsState(Int).value),
        DataType(Type.ERASMUS, viewModel.erasmusAmount.observeAsState(Int).value,viewModel.maxErasmus.observeAsState(Int).value),
    )

    AnalyticsBody(
        items = typeList,
        amountsTotal = viewModel.totalAmount.observeAsState(Int).value,
        maxTotal = viewModel.maxTotal.observeAsState(Int).value,
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
    maxTotal: Any,
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