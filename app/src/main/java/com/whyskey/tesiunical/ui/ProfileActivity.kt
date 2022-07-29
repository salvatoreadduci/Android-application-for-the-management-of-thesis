package com.whyskey.tesiunical.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Language
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.ThesisCard
import com.whyskey.tesiunical.ui.components.ThesisRow

@Composable
fun Profile(
    onClickSeeAll: (String) -> Unit = {},
    allCompilation: List<Thesis>,
    allExperimental: List<Thesis>,
    allResearch: List<Thesis>,
    viewModel: ThesisViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard(viewModel)
        Spacer(Modifier.height(16.dp))
        CompilationThesisCard(onClickSeeAll = onClickSeeAll,allCompilation,viewModel)
        Spacer(Modifier.height(16.dp))
        ApplicationThesisCard(onClickSeeAll = onClickSeeAll,allExperimental,viewModel)
        Spacer(Modifier.height(16.dp))
        ApplicationThesisCard(onClickSeeAll = onClickSeeAll,allResearch,viewModel)
    }
}

@Composable
private fun ProfileCard(
    viewModel: ThesisViewModel
){

    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://${viewModel.userData.value.web_site}"))
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(viewModel.userData.value.email))
    }
    val context = LocalContext.current

    Card(backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)) {
            Image(
                painter = painterResource(id = R.drawable.io),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(88.dp)
                    .clip(CircleShape)

            )

            Text(viewModel.userData.value.name)

            Row(Modifier.padding(8.dp)) {

                Icon(
                    Icons.Rounded.Language ,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { ContextCompat.startActivity(context, webIntent, null) }
                )
                Icon(
                    Icons.Rounded.Email ,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable { ContextCompat.startActivity(context, emailIntent, null) }
                )
            }
        }
    }
}

@Composable
private fun CompilationThesisCard(
    onClickSeeAll: (String) -> Unit,
    list: List<Thesis>,
    viewModel: ThesisViewModel
){
    var expandedThesis by remember { mutableStateOf<String?>(null) }
    val title = stringResource(id = R.string.compilation_thesis)

    ThesisCard(
        title = title,
        onClickSeeAll =  { onClickSeeAll(title) } ,
        data = list
    ){
            thesis ->
        ThesisRow(
            name = thesis.title,
            expanded = expandedThesis == thesis.title,
            onClick = { expandedThesis = if (expandedThesis == thesis.title) null else thesis.title },
            onDelete = { viewModel.removeThesis(thesis.id) }
        )
    }
}

@Composable
private fun ApplicationThesisCard(
    onClickSeeAll: (String) -> Unit,
    list: List<Thesis>,
    viewModel: ThesisViewModel
){
    var expandedThesis by remember { mutableStateOf<String?>(null) }
    val title = stringResource(id = R.string.application_thesis)

    ThesisCard(
        title = title,
        onClickSeeAll = { onClickSeeAll(title) },
        data = list
    ){
            thesis ->
        ThesisRow(
            name = thesis.title,
            expanded = expandedThesis == thesis.title,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
            },
            onDelete = { viewModel.removeThesis(thesis.id) }
        )
    }
}

@Composable
private fun ResearchThesisCard(
    onClickSeeAll: (String) -> Unit,
    list: List<Thesis>,
    viewModel: ThesisViewModel
){
    var expandedThesis by remember { mutableStateOf<String?>(null) }
    val title = stringResource(id = R.string.application_thesis)

    ThesisCard(
        title = title,
        onClickSeeAll = { onClickSeeAll(title) },
        data = list
    ){
            thesis ->
        ThesisRow(
            name = thesis.title,
            expanded = expandedThesis == thesis.title,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
            },
            onDelete = { viewModel.removeThesis(thesis.id) }
        )
    }
}