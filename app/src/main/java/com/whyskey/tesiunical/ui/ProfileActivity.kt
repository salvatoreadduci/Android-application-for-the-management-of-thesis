package com.whyskey.tesiunical.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.ThesisCard
import com.whyskey.tesiunical.ui.components.ThesisRow
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

@Composable
fun Profile(
    onClickSeeAll: (String) -> Unit = {},
    allCompilation: List<Thesis>,
    allExperimental: List<Thesis>,
    viewModel: ThesisViewModel
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard()
        Spacer(Modifier.height(16.dp))
        CompilationThesisCard(onClickSeeAll = onClickSeeAll,allCompilation,viewModel)
        Spacer(Modifier.height(16.dp))
        ExperimentalThesisCard(onClickSeeAll = onClickSeeAll,allExperimental,viewModel)
    }
}

@Composable
private fun ProfileCard(){

    val webIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com"))
    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("mario@example.com"))
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

            Text("Nome Cognome")

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
            name = thesis.name,
            expanded = expandedThesis == thesis.name,
            onClick = { expandedThesis = if (expandedThesis == thesis.name) null else thesis.name },
            onDelete = { viewModel.removeThesis(thesis) }
        )
    }
}

@Composable
private fun ExperimentalThesisCard(
    onClickSeeAll: (String) -> Unit,
    list: List<Thesis>,
    viewModel: ThesisViewModel
){
    var expandedThesis by remember { mutableStateOf<String?>(null) }
    val title = stringResource(id = R.string.experimental_thesis)

    ThesisCard(
        title = title,
        onClickSeeAll = { onClickSeeAll(title) },
        data = list
    ){
            thesis ->
        ThesisRow(
            name = thesis.name,
            expanded = expandedThesis == thesis.name,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.name) null else thesis.name
            },
            onDelete = { viewModel.removeThesis(thesis) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    TesiUnicalTheme {
        //Profile(onClickSeeAll = {}, list = )
    }
}