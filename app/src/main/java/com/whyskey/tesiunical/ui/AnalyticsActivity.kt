package com.whyskey.tesiunical.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Session
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.AnalyticsRow

@Composable
fun Analytics(
    viewModel: ThesisViewModel
) {


    for(temp in 0..3){
        viewModel.session(temp)
    }
    val list: List<Session> = listOf(
        viewModel.marchSession.value,
        viewModel.julySession.value,
        viewModel.septemberSession.value,
        viewModel.decemberSession.value,
    )

    var count = 0
    var title = ""

    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

        list.forEach {
            when (count) {
                0 -> title = stringResource(id = R.string.march_session)
                1 -> title = stringResource(id = R.string.july_session)
                2 -> title = stringResource(id = R.string.september_session)
                3 -> title = stringResource(id = R.string.december_session)
            }

            count++
            AnalyticsBody(
                viewModel = viewModel,
                title = title,
                applicative = it.applicative,
                compilation = it.compilation,
                corporate = it.corporate,
                erasmus = it.erasmus,
                research = it.research
            )
        }
        Spacer(modifier = Modifier.height(80.dp))
    }

}

@Composable
fun AnalyticsBody(
    modifier: Modifier = Modifier,
    viewModel: ThesisViewModel,
    title: String,
    applicative: Map<String,Int>,
    compilation: Map<String,Int>,
    corporate: Map<String,Int>,
    erasmus: Map<String,Int>,
    research: Map<String,Int>,
){
    Spacer(modifier.height(10.dp))
    Card {
        Column {

            Column(Modifier.padding(start = 8.dp, top = 8.dp)) {
                Text(text = title)
            }

            Column(modifier = modifier.padding(8.dp)) {
                val list = mapOf(
                    stringResource(id = R.string.application_thesis) to applicative,
                    stringResource(id = R.string.compilation_thesis) to compilation,
                    stringResource(id = R.string.corporate_thesis) to corporate,
                    stringResource(id = R.string.erasmus_thesis) to erasmus,
                    stringResource(id = R.string.research_thesis) to research
                )

                list.forEach{
                    AnalyticsRow(viewModel = viewModel, type = it.key, amount = it.value["current"], max = it.value["max"])
                }
            }
        }
    }
}