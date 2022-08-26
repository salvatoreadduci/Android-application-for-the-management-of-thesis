package com.whyskey.tesiunical.ui

import android.content.Intent
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import kotlin.math.exp

@Composable
fun Profile(
    onClickSeeAll: (String) -> Unit = {},
    viewModel: ThesisViewModel,
    id:String
) {

    val profile = if(id == viewModel.userData.value.id){
        viewModel.userData.value
    } else {
        val temp = viewModel.accounts.value.find { account -> id == account.id}
            if(temp != null){
                viewModel.getThesis(temp.id,0)
                viewModel.getThesis(temp.id,1)
                viewModel.getThesis(temp.id,2)
                viewModel.getThesis(temp.id,3)
                viewModel.getThesis(temp.id,4)
                temp
        } else {
            viewModel.account(id)
            viewModel.visitedAccount.value
        }
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        ProfileCard(viewModel,profile)
        Spacer(Modifier.height(16.dp))
        if(profile.isProfessor){
            CompilationThesisCard(onClickSeeAll = onClickSeeAll,viewModel.compilationThesis.value,viewModel,profile)
            Spacer(Modifier.height(16.dp))
            ApplicationThesisCard(onClickSeeAll = onClickSeeAll,viewModel.applicationThesis.value,viewModel,profile)
            Spacer(Modifier.height(16.dp))
            ResearchThesisCard(onClickSeeAll = onClickSeeAll,viewModel.researchThesis.value,viewModel,profile)
            Spacer(modifier = Modifier.height(80.dp))
        } else {
            var expandedThesis by remember { mutableStateOf<String?>(null) }
            AssignedThesis(
                profile,
                viewModel,
                expandedThesis == viewModel.thesis.value.id,
                onClick = {
                    expandedThesis = if (expandedThesis == viewModel.thesis.value.id) null else viewModel.thesis.value.id
                }
            )
            Spacer(Modifier.height(16.dp))
            NotPassedExams(profile)
            
        }
    }
}

@Composable
private fun ProfileCard(
    viewModel: ThesisViewModel,
    profile: Account
){

    viewModel.getImage(profile)
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

    var show by rememberSaveable { mutableStateOf(false) }
    var thesisId by rememberSaveable { mutableStateOf("") }

    ConfirmDeleteDialog(
        show = show,
        onDismiss = { show = false },
        onRemove = { viewModel.removeThesis(thesisId)
        show = false
        }
    )

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
            description = thesis.description,
            expanded = expandedThesis == thesis.title,
            onClick = { expandedThesis = if (expandedThesis == thesis.title) null else thesis.title },
            onDelete = {
                show = true
                thesisId = thesis.id
                       },
            onRequest = {
                viewModel.addNewRequest(viewModel.userData.value.id, profile.id,thesis.id,viewModel.userData.value.name, 0, thesis.title,viewModel.userData.value.email,thesis)
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

    var show by rememberSaveable { mutableStateOf(false) }
    var thesisId by rememberSaveable { mutableStateOf("") }

    ConfirmDeleteDialog(
        show = show,
        onDismiss = { show = false },
        onRemove = { viewModel.removeThesis(thesisId)
            show = false
        }
    )

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
            description = thesis.description,
            expanded = expandedThesis == thesis.title,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
            },
            onDelete = {
                show = true
                thesisId = thesis.id
            },
            onRequest = {
                viewModel.addNewRequest(viewModel.userData.value.id, profile.id,thesis.id,viewModel.userData.value.name, 0, thesis.title,viewModel.userData.value.email,thesis)
            }
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
    val title = stringResource(id = R.string.research_thesis)

    var show by rememberSaveable { mutableStateOf(false) }
    var thesisId by rememberSaveable { mutableStateOf("") }

    ConfirmDeleteDialog(
        show = show,
        onDismiss = { show = false },
        onRemove = { viewModel.removeThesis(thesisId)
            show = false
        }
    )

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
            description = thesis.description,
            expanded = expandedThesis == thesis.title,
            onClick = {
                expandedThesis = if (expandedThesis == thesis.title) null else thesis.title
            },
            onDelete = {
                show = true
                thesisId = thesis.id
            },
            onRequest = {
                viewModel.addNewRequest(viewModel.userData.value.id, profile.id,thesis.id,viewModel.userData.value.name, 0, thesis.title,viewModel.userData.value.email,thesis)
            }
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
                if(profile.hasThesis){
                    viewModel.returnThesis()

                    Text(text = viewModel.thesis.value.title)

                } else {
                    Text(text = stringResource(id = R.string.not_assigned))
                }

            }
            if (expanded && profile.hasThesis) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viewModel.thesis.value.description,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(8.dp))
                val type = when(viewModel.thesis.value.type){
                    0 -> stringResource(id = R.string.compilation_thesis)
                    1 -> stringResource(id = R.string.application_thesis)
                    2 -> stringResource(id = R.string.corporate_thesis)
                    3 -> stringResource(id = R.string.erasmus_thesis)
                    else -> stringResource(id = R.string.research_thesis)
                }

                Text(text = "${stringResource(id = R.string.type)} $type")
                Spacer(modifier = Modifier.height(8.dp))
                val prof = viewModel.accounts.value.find { prof -> prof.id == viewModel.thesis.value.id_professor }
                Text(text = "${stringResource(id = R.string.professor)} ${prof?.name}")
            }
        }
    }
}

@Composable
private fun NotPassedExams(
    profile: Account
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "${stringResource(id = R.string.change_exams)}:")
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = profile.exams)
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onRemove: () -> Unit,
){
    if(show){
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onRemove  )
                { Text(text = stringResource(id = R.string.delete)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = stringResource(id = R.string.confirm_delete)) }
        )
            
    }
}