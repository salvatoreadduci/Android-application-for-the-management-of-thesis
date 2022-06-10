package com.whyskey.tesiunical.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Info
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Thesis
import com.whyskey.tesiunical.ui.theme.TesiUnicalTheme

@Composable
fun Profile(
    onClickSeeAll: () -> Unit = {},
    list: List<Thesis>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard()
        Spacer(Modifier.height(16.dp))
        ApplicativeThesisCard(onClickSeeAll,list)
        Spacer(Modifier.height(16.dp))
        ComparativeThesisCard(onClickSeeAll,list)
    }
}



@Composable
private fun ProfileCard(){

    val webIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.android.com"))
    val emailIntent: Intent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf("jan@example.com"))
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
private fun <T> TemplateThesisCard(
    title: String,
    onClickSeeAll: () -> Unit,
    data: List<T>,
    row: @Composable (T) -> Unit

){
    Card{
        Column {
            Column(Modifier.padding(16.dp)) {
                Text(text = title)
            }

            Column(Modifier.padding(start = 8.dp, top = 4.dp, end = 8.dp)) {
                data.take(SHOWN_ITEMS).forEach { row(it) }
            }

            SeeAllButton(
                modifier = Modifier.clearAndSetSemantics {
                    contentDescription = "All $title"
                },
                onClick = onClickSeeAll,
            )
        }
    }

}

@Composable
private fun ApplicativeThesisCard(
    onClickSeeAll: () -> Unit,
    list: List<Thesis>
){
    var expandedThesis by remember { mutableStateOf<String?>(null) }

    TemplateThesisCard(
        title = stringResource(id = R.string.applications_thesis),
        onClickSeeAll =  onClickSeeAll ,
        data = list
    ){
            thesis ->
        ThesisRow(
            name = thesis.name,
            expanded = expandedThesis == thesis.name,
            onClick = { expandedThesis = if (expandedThesis == thesis.name) null else thesis.name }
        )
    }
}

@Composable
private fun ComparativeThesisCard(
    onClickSeeAll: () -> Unit,
    list: List<Thesis>
){
    var expandedThesis by remember { mutableStateOf<String?>(null) }

    TemplateThesisCard(
        title = stringResource(id = R.string.comparative_thesis),
        onClickSeeAll = { onClickSeeAll() },
        data = list
    ){
            thesis ->
        ThesisRow(
            name = thesis.name,
            expanded = expandedThesis == thesis.name,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.name) null else thesis.name
            }
        )
    }

}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ThesisRow(name: String, expanded: Boolean, onClick: () -> Unit) {
    ThesisRowSpacer(visible = expanded)
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 2.dp,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row {
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = name,
                    style = MaterialTheme.typography.body1
                )
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.lorem_ipsum),
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
    ThesisRowSpacer(visible = expanded)
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ThesisRowSpacer(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun SeeAllButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .height(44.dp)
            .fillMaxWidth()
    ) {
        Text(stringResource(R.string.see_all))
    }
}

private const val SHOWN_ITEMS = 3

@Preview(showBackground = true)
@Composable
fun ProfilePreview() {
    TesiUnicalTheme {

    }
}