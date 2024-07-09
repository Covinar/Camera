package com.example.gallery.presentation.preview

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.common.models.Image
import com.example.gallery.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    image: Image,
    onDeleteClicked: () -> Unit,
    onCloseClicked: () -> Unit
) {

    TopAppBar(
        title = {
            Column() {
                Text(
                    text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(image.date * 1000)),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp, top = 24.dp)
                )
                Text(
                    text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(image.date * 1000)),
                    modifier = Modifier.padding(start = 8.dp, bottom = 24.dp),
                )
            }

        },
        actions = {
            Icon(
                painter = painterResource(id = R.drawable.ic_delete),
                contentDescription = null,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable {
                    onDeleteClicked()
                }
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_close),
                contentDescription = null,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .clickable {
                    onCloseClicked()
                }
            )
        }
    )
}