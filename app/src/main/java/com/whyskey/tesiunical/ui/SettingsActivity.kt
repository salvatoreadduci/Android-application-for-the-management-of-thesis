package com.whyskey.tesiunical.ui


import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Session
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.ChangeLimitDialog
import com.whyskey.tesiunical.ui.components.ChangeOptionDialog
import com.whyskey.tesiunical.ui.components.LimitThesisRow
import com.whyskey.tesiunical.ui.components.SettingsRow


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Settings(
    viewModel: ThesisViewModel
) {
    viewModel.getImage()

    val openDialog = remember { mutableStateOf(false) }
    ChangeThesisDialog(openDialog = openDialog,viewModel = viewModel)

    ChangeOptionDialog(
        show = viewModel.showOptionNameDialog.collectAsState().value,
        title = stringResource(id = R.string.change_name),
        onDismiss =  { viewModel.onOptionDialogConfirm() },
        onConfirm =  viewModel::changeName,
        viewModel = viewModel
    )

   ChangeOptionDialog(
        show = viewModel.showOptionEmailDialog.collectAsState().value,
        title = stringResource(id = R.string.change_email),
        onDismiss =  { viewModel.onOptionDialogConfirm() },
        onConfirm = viewModel::changeEmail,
        viewModel = viewModel
   )

    ChangeOptionDialog(
        show = viewModel.showOptionWebDialog.collectAsState().value,
        title = stringResource(id = R.string.change_web),
        onDismiss =  { viewModel.onOptionDialogConfirm() },
        onConfirm = viewModel::changeWebSite,
        viewModel = viewModel
    )
    
    val settingsList: List<SettingsData> = listOf(
        SettingsData(
            Icons.Rounded.Person,
            stringResource(id = R.string.name),
            viewModel.userData.value.name,
            0
        ),
        SettingsData(
            Icons.Rounded.Email,
            stringResource(id = R.string.email),
            viewModel.userData.value.email,
            1
        ),
        SettingsData(
            Icons.Rounded.Language,
            stringResource(id = R.string.web_site),
            viewModel.userData.value.web_site,
            2
        )
    )

    var uri by remember {
        mutableStateOf(viewModel.userImage.value)
    }
    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        viewModel.storage.child("images/" + viewModel.user!!.uid).putFile(it)
        uri = it
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = uri),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 8.dp)
                .size(154.dp)
                .clip(CircleShape)
                .clickable {
                    launcher.launch("image/*")
                }
        )
        Spacer(Modifier.height(8.dp))

        SettingsBody(
            title = stringResource(id = R.string.account_settings),
            items = settingsList,
            rows = {
                if(
                    (it.title != stringResource(id = R.string.web_site) || viewModel.userData.value.isProfessor)
                ){
                    SettingsRow(
                        image = it.image,
                        title = it.title,
                        value = it.value,
                        onClick = {
                            viewModel.onOptionDialogClicked(it.dialog)
                        }
                    )
                }
            }
        )
        Spacer(Modifier.height(8.dp))

        if(viewModel.userData.value.isProfessor){

            val thesisList: List<SettingsData> = listOf(
                SettingsData(
                    Icons.Rounded.EmojiNature,
                    stringResource(id = R.string.march_session),
                    viewModel.marchSession.value,
                    0
                ),
                SettingsData(
                    Icons.Rounded.BeachAccess,
                    stringResource(id = R.string.july_session),
                    viewModel.julySession.value,
                    1
                ),
                SettingsData(
                    Icons.Rounded.Park,
                    stringResource(id = R.string.september_session),
                    viewModel.septemberSession.value,
                    2
                ),
                SettingsData(
                    Icons.Rounded.AcUnit,
                    stringResource(id = R.string.december_session),
                    viewModel.decemberSession.value,
                    3
                )
            )

            for(temp in 0..3){
                viewModel.session(temp)
            }

            var title by rememberSaveable { mutableStateOf("") }
            var list by remember { mutableStateOf(viewModel.decemberSession.value)}
            var idList by remember { mutableStateOf("3")}


            ChangeLimitDialog(
                show = viewModel.showLimitDialog.collectAsState().value,
                title = title,
                onDismiss = { viewModel.onOptionDialogConfirm() },
                onConfirm = viewModel::changeLimit,
                item = list,
                idList = idList,
                viewModel = viewModel
            )

            LimitThesisBody(
                title = stringResource(id = R.string.thesis_limit),
                items = thesisList
            ) {
                SettingsRow(
                    image = it.image,
                    title = it.title,
                    value = it.title,
                    onClick = {
                        title = it.title
                        idList = it.dialog.toString()
                        list = it.value as Session
                        viewModel.onOptionDialogClicked(3)
                    }
                )
            }
        } else {
            Card{
                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                        openDialog.value = !openDialog.value
                    }
                ){
                    Row(
                        modifier = Modifier
                            .height(68.dp)
                            .padding(start = 4.dp, end = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.School,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = stringResource(id = R.string.change_exams))
                        Spacer(modifier = Modifier.weight(1f))
                        Icon(
                            imageVector = Icons.Filled.Edit,
                            contentDescription = "Edit"
                        )
                    }
                }
            }

        }

    }
}

@Composable
fun <T> SettingsBody(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    rows: @Composable (T) -> Unit
){
    Card {
        Column {
            Text(
                text = title,
                modifier = modifier.padding(4.dp)
            )
            items.forEach { item ->
                rows(item)
            }
        }
    }
}

class SettingsData(
    val image: ImageVector,
    val title: String,
    val value: Any,
    var dialog: Int
)

class LimitThesis(
    val title: String,
    val value: Any
)

@Composable
fun <T> LimitThesisBody(
    modifier: Modifier = Modifier,
    title: String,
    items: List<T>,
    rows: @Composable (T) -> Unit
){
    Card {
        Column(modifier = modifier.fillMaxWidth()) {
            Row(modifier = modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    modifier = modifier.padding(4.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(checked = false, onCheckedChange =  {} )
            }

            items.forEach { item ->
                rows(item)
            }
            
        }
    }
}

@Composable
private fun ChangeThesisDialog(
    openDialog: MutableState<Boolean>,
    viewModel: ThesisViewModel
){
    var examsInput by rememberSaveable { mutableStateOf(viewModel.userData.value.exams) }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = {
                Text(text = stringResource(id = R.string.change_exams))
            },
            text = {
                TextField(
                    modifier = Modifier.height(180.dp),
                    value = examsInput,
                    onValueChange = {examsInput = it},
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                       viewModel.setExams(examsInput)
                        openDialog.value = false
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }
                ) {
                    Text("Dismiss")
                }
            }
        )
    }

}