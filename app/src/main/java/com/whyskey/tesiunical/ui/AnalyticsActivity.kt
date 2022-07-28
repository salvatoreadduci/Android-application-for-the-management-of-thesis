package com.whyskey.tesiunical.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

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
        DataType(Type.COMPILATION, viewModel.compilationThesis.value.size, viewModel.userData.value.max_compilation),
        DataType(Type.APPLICATION, viewModel.compilationThesis.value.size,viewModel.userData.value.max_applicative),
        DataType(Type.RESEARCH, viewModel.compilationThesis.value.size,viewModel.userData.value.max_research),
        DataType(Type.CORPORATE, viewModel.compilationThesis.value.size,viewModel.userData.value.max_corporate),
        DataType(Type.ERASMUS, viewModel.compilationThesis.value.size,viewModel.userData.value.max_erasmus),
    )

   AnalyticsBody(
        items = typeList,
        amountsTotal =  viewModel.compilationThesis.value.size +
                viewModel.compilationThesis.value.size +
                viewModel.compilationThesis.value.size +
                viewModel.compilationThesis.value.size +
                viewModel.compilationThesis.value.size,
        maxTotal =  viewModel.userData.value.max_compilation +
                viewModel.userData.value.max_applicative +
                viewModel.userData.value.max_research +
                viewModel.userData.value.max_corporate +
                viewModel.userData.value.max_erasmus
       ,
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
    amountsTotal: Int,
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