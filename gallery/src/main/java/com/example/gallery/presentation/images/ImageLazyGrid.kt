package com.example.gallery.presentation.images

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.common.models.Image
import com.example.gallery.presentation.ImagesViewModel

@Composable
fun ImageLazyGrid(
    modifier: Modifier,
    lazyGridState: LazyGridState,
    state: ImagesViewModel.State,
    onImageClicked: (image: Image) -> Unit,
){
    LazyVerticalGrid(
        modifier = modifier,
        state = lazyGridState,
        columns = GridCells.Fixed(3)
    ) {
        items(
            items = state.images,
            //key = { it.path }
        ) {image ->
            ImageItem(
                image = image,
                isEditMode = state.isEditMode,
                isChecked = state.isChecked(image),
                onImageClicked = {
                    onImageClicked(image)
                }
            )
        }
    }
}