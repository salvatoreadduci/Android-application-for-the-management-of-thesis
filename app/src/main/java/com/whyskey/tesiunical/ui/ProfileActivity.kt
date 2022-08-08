package com.whyskey.tesiunical.ui

import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Account
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
    viewModel: ThesisViewModel,
    id:String
) {


    val profile = if(id == viewModel.userData.value.id){
        viewModel.userData.value
    } else {
        viewModel.accounts.value.find { account -> id == account.id }
            ?: viewModel.accountsToAccept.value.find { account -> id == account.id }!!
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard(viewModel,profile)
        Spacer(Modifier.height(16.dp))
        if(profile.isProfessor){
            viewModel.getThesis(profile.id,0)
            viewModel.getThesis(profile.id,1)
            viewModel.getThesis(profile.id,2)
            viewModel.getThesis(profile.id,3)
            viewModel.getThesis(profile.id,4)
            CompilationThesisCard(onClickSeeAll = onClickSeeAll,allCompilation,viewModel,profile)
            Spacer(Modifier.height(16.dp))
            ApplicationThesisCard(onClickSeeAll = onClickSeeAll,allExperimental,viewModel,profile)
            Spacer(Modifier.height(16.dp))
            ResearchThesisCard(onClickSeeAll = onClickSeeAll,allResearch,viewModel,profile)
        } else {
            var expandedThesis by remember { mutableStateOf<String?>(null) }
            AssignedThesis(
                profile,
                viewModel,
                expandedThesis == viewModel.userData.value.thesis,
                onClick = {
                    expandedThesis = if (expandedThesis == viewModel.userData.value.thesis) null else viewModel.userData.value.thesis
                }
            )
            Spacer(Modifier.height(16.dp))
            NotPassedExams(profile,viewModel)
            
        }
    }
}

@Composable
private fun ProfileCard(
    viewModel: ThesisViewModel,
    profile: Account
){

    viewModel.getImage()
    val webIntent = if(profile.isProfessor){
        Intent(Intent.ACTION_VIEW, Uri.parse("http://${profile.web_site}"))
    } else {
        Intent()
    }

    val emailIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_EMAIL, arrayOf(profile.email))
    }
    val context = LocalContext.current

    Card(backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp)) {
            Image(
                painter = rememberAsyncImagePainter(model = viewModel.userImage.value),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(140.dp)
                    .clip(CircleShape)
            )
            Text(profile.name)
            Row(Modifier.padding(8.dp)) {

                if(profile.isProfessor) {
                    Icon(
                        Icons.Rounded.Language,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable { ContextCompat.startActivity(context, webIntent, null) }
                    )
                }
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
    viewModel: ThesisViewModel,
    profile: Account,
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
            viewModel = viewModel,
            profile = profile,
            name = thesis.title,
            expanded = expandedThesis == thesis.title,
            onClick = { expandedThesis = if (expandedThesis == thesis.title) null else thesis.title },
            onDelete = { viewModel.removeThesis(thesis.id) },
            onRequest = {
                viewModel.addNewRequest(viewModel.userData.value.id, profile.id,thesis.id)
                viewModel.getThesis(profile.id, thesis.type)
            }
        )
    }
}

@Composable
private fun ApplicationThesisCard(
    onClickSeeAll: (String) -> Unit,
    list: List<Thesis>,
    viewModel: ThesisViewModel,
    profile: Account
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
            viewModel = viewModel,
            profile = profile,
            name = thesis.title,
            expanded = expandedThesis == thesis.title,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
            },
            onDelete = { viewModel.removeThesis(thesis.id) },
            onRequest = { viewModel.addNewRequest(viewModel.userData.value.id, profile.id,thesis.id) }
        )
    }
}

@Composable
private fun ResearchThesisCard(
    onClickSeeAll: (String) -> Unit,
    list: List<Thesis>,
    viewModel: ThesisViewModel,
    profile: Account
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
            viewModel = viewModel,
            profile = profile,
            name = thesis.title,
            expanded = expandedThesis == thesis.title,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
            },
            onDelete = { viewModel.removeThesis(thesis.id) },
            onRequest = {viewModel.addNewRequest(viewModel.userData.value.id, profile.id,thesis.id) }
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun AssignedThesis(
    profile: Account,
    viewModel: ThesisViewModel,
    expanded: Boolean,
    onClick: () -> Unit,
){
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = 2.dp,
        onClick = onClick
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(
                modifier = Modifier
                    .height(40.dp)
                    .padding(start = 4.dp, end = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ){
                Icon(
                    imageVector = Icons.Rounded.Info,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = stringResource(id = R.string.assigned_thesis))
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = profile.thesis)
            }

            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.lorem_ipsum),
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(id = R.string.type))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = stringResource(id = R.string.professor))
            }
        }
    }
}

@Composable
private fun NotPassedExams(
    profile: Account,
    viewModel: ThesisViewModel
){
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ){
            Text(text = "${stringResource(id = R.string.change_exams)}:")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = profile.exams)
        }
    }
}