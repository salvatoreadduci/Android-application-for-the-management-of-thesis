package com.whyskey.tesiunical.ui.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.whyskey.tesiunical.R
import com.whyskey.tesiunical.data.Account
import com.whyskey.tesiunical.model.ThesisViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ThesisRow(
    viewModel: ThesisViewModel,
    profile: Account,
    name: String,
    description: String,
    expanded: Boolean,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onRequest: () -> Unit
) {
    viewModel.returnThesis(viewModel.userData.value.id)
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
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier.widthIn(1.dp,250.dp)
                )
                Spacer(Modifier.weight(1f))
                if(viewModel.userData.value.isProfessor){
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = "Close",
                        modifier = Modifier.clickable { onDelete() }
                    )
                }
                
            }
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    textAlign = TextAlign.Justify
                )

                if(!viewModel.userData.value.isProfessor && !viewModel.userData.value.hasThesis
                    && viewModel.thesis.value.size < 3 && viewModel.thesis.value.find { thesis -> thesis.id_professor != profile.id } == null){
                    Button(onClick = { onRequest() }) {
                        Text(text = stringResource(id = R.string.request))
                    }
                }
            }
        }
    }
    ThesisRowSpacer(visible = expanded)
}

@Composable
fun ThesisRowSpacer(visible: Boolean) {
    AnimatedVisibility(visible = visible) {
        Spacer(modifier = Modifier.height(8.dp))
    }
}