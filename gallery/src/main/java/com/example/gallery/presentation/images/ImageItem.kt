package com.example.gallery.presentation.images

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.common.models.Image
import com.example.gallery.R

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImageItem(
    image: Image,
    isEditMode: Boolean,
    isChecked: Boolean,
    onImageClicked: () -> Unit,
) {

    Box(
        modifier = Modifier
            .padding(2.dp)
            .clickable {
                onImageClicked()
            }
    ) {
        GlideImage(
            model = image.path,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(156.dp),
            contentScale = ContentScale.Crop
        )

        val painter = if (isChecked) R.drawable.ic_check else R.drawable.ic_uncheck

        if (isEditMode) {
            Image(
                painter = painterResource(id = painter),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(top = 8.dp, end = 8.dp)
            )
        }
    }

}