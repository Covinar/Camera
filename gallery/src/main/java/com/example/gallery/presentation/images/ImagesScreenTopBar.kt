package com.example.gallery.presentation.images

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.AbsoluteRoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.gallery.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    checkedNotesCount: Int,
    isEditMode: Boolean,
    isCheckedMoreThenOne: Boolean,
    onEditClicked: () -> Unit,
    onCloseClicked: () -> Unit,
    onDeleteButtonClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    Column() {
        TopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.top_bar_images_title),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
            actions = {
                if (!isEditMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_edit),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .clickable {
                                onEditClicked()
                            }
                    )
                } else if (isCheckedMoreThenOne) {
                    Button(
                        onClick = {
                            onDeleteButtonClicked()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = Color.Black
                        ),
                        shape = AbsoluteRoundedCornerShape(8.dp),
                        border = BorderStroke(2.dp, Color.Black)
                    ) {
                        Text(
                            text = stringResource(id = R.string.top_bar_button, checkedNotesCount),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            },
            navigationIcon = {
                if (!isEditMode) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                onBackClicked()
                            }
                    )
                } else {
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

            },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White
            )
        )
        Divider(
            thickness = 2.dp,
            color = Color.Black
        )
    }

}
