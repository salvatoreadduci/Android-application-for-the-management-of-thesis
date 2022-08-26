package com.whyskey.tesiunical.ui.components


import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Request
import com.whyskey.tesiunical.model.ThesisViewModel


@Composable
fun AccountCollection(
    title: String,
    profileCollection: List<Request>,
    viewModel: ThesisViewModel,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .heightIn(min = 56.dp)
                .padding(start = 24.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.h6,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .wrapContentWidth(Alignment.Start)
            )
        }

        LazyRow(
            modifier = modifier,
            contentPadding = PaddingValues(start = 12.dp, end = 12.dp)
        ) {
            items(profileCollection){
                var show by rememberSaveable { mutableStateOf(false) }
                var email by rememberSaveable { mutableStateOf("") }
                val context = LocalContext.current
                ConfirmDeclineDialog(
                    show = show,
                    onDismiss = { show = false
                        viewModel.changeRequest(it.id,it.id_student,it.id_thesis,it.session,false,context)},
                    onClick = {
                        val emailIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                        }
                        ContextCompat.startActivity(context, emailIntent, null)
                        viewModel.changeRequest(it.id,it.id_student,it.id_thesis,it.session,false,context)
                        show = false
                    }
                )
                ProfileItem(profile = it, viewModel = viewModel, onClick = onClick, show, {show = true
                    email = it.email})
            }
        }
    }
}

@Composable
fun ProfileItem(
    profile: Request,
    viewModel: ThesisViewModel,
    onClick: (String) -> Unit,
    show: Boolean,
    onShow: () -> Unit,
    modifier: Modifier = Modifier
) {
    //viewModel.getImage(profile.id)

    Card(
        backgroundColor = MaterialTheme.colors.primary,
        shape = MaterialTheme.shapes.medium,
        modifier = modifier.padding(
            start = 4.dp,
            end = 4.dp,
            bottom = 8.dp
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .clickable(onClick = {
                    if(!viewModel.userData.value.isProfessor){
                        onClick(profile.id_professor)
                    } else {
                        onClick(profile.id_student)
                    }
                })
                .padding(8.dp)
        ) {
            ProfileImage(
                imageUrl = profile.image,
                elevation = 4.dp,
                contentDescription = null,
                modifier = Modifier.size(120.dp)
            )
            Text(
                text = profile.name,
                style = MaterialTheme.typography.subtitle1,
                color = MaterialTheme.colors.onSecondary,
                modifier = Modifier.padding(top = 8.dp)
            )

            if(viewModel.userData.value.isProfessor){
                Text(
                    text = profile.thesis,
                    style = MaterialTheme.typography.subtitle2,
                    color = MaterialTheme.colors.onSecondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                if(!profile.accepted){
                    Text(
                        text = profile.session.toString(),
                        style = MaterialTheme.typography.subtitle2,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    Row(){
                        val context = LocalContext.current
                        Icon(
                            Icons.Filled.Done,
                            contentDescription = "Done",
                            modifier = Modifier.clickable {
                                viewModel.changeRequest(profile.id,profile.id_student,profile.id_thesis,profile.session, true, context)
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Close",
                            modifier = Modifier.clickable {
                                onShow()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileImage(
    imageUrl: String,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp
) {
    JetsnackSurface(
        color = Color.LightGray,
        elevation = elevation,
        shape = CircleShape,
        modifier = modifier
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = contentDescription,
            placeholder = painterResource(id = R.drawable.user),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun ConfirmDeclineDialog(
    show: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
){
    if(show){
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = onClick )
                { Text(text = stringResource(id = R.string.send)) }
            },
            dismissButton = {
                TextButton(onClick = onDismiss)
                { Text(text = stringResource(id = R.string.cancel)) }
            },
            title = { Text(text = stringResource(id = R.string.send_message)) }
        )
    }
}