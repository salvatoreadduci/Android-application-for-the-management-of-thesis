package com.whyskey.tesiunical.ui


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.model.ThesisViewModel
import com.whyskey.tesiunical.ui.components.ChangeOptionDialog
import com.whyskey.tesiunical.ui.components.LimitThesisRow
import com.whyskey.tesiunical.ui.components.SettingsRow


@Composable
fun Settings(
    viewModel: ThesisViewModel
) {
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

    val list: List<LimitThesis> = listOf(
        LimitThesis(
            stringResource(id = R.string.compilation_thesis),
            viewModel.userData.value.max_compilation
        ),
        LimitThesis(
            stringResource(id = R.string.application_thesis),
            viewModel.userData.value.max_applicative
        ),
        LimitThesis(
            stringResource(id = R.string.research_thesis),
            viewModel.userData.value.max_research
        ),
        LimitThesis(
            stringResource(id = R.string.corporate_thesis),
            viewModel.userData.value.max_corporate
        ),
        LimitThesis(
            stringResource(id = R.string.erasmus_thesis),
            viewModel.userData.value.max_erasmus
        )
    )

    var selectedImage by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()){
        selectedImage = it
    }



    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = rememberAsyncImagePainter(model = selectedImage),
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
                SettingsRow(
                    image = it.image,
                    title = it.title,
                    value = it.value,
                    onClick = {
                        viewModel.onOptionDialogClicked(it.dialog)
                    }
                )
            }
        )
        Spacer(Modifier.height(8.dp))
        
        LimitThesisBody(
            title = stringResource(id = R.string.thesis_limit),
            items = list
        ) {
            LimitThesisRow(
                title = it.title,
                value = it.value,
                viewModel = viewModel
                )
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