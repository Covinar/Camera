package com.example.gallery.presentation.images

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gallery.presentation.ImagesViewModel
import com.example.gallery.presentation.common.DeleteDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagesScreen(
    viewModel: ImagesViewModel = hiltViewModel(),
    openPreview: () -> Unit,
    openCamera: () -> Unit
) {

    val state by viewModel.state.collectAsState()

    val lazyGridState = rememberLazyGridState()

    LaunchedEffect(key1 = "image_screen") {
        viewModel.getImages()
    }

    Scaffold(
        topBar = { TopBar(
            onEditClicked = {
                viewModel.changeEditMode()
            },
            isEditMode = state.isEditMode,
            isCheckedMoreThenOne = state.checkedImages.isNotEmpty(),
            checkedNotesCount = state.checkedImages.size,
            onCloseClicked = {
                viewModel.changeEditMode()
                viewModel.clearSelected()
            },
            onDeleteButtonClicked = {
                viewModel.changeDeleteMode()
            },
            onBackClicked = {
                openCamera()
            }
        ) }
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            ImageLazyGrid(
                modifier = Modifier.padding(it),
                lazyGridState = lazyGridState,
                state = state,
                onImageClicked = { image ->
                    if (state.isEditMode) {
                        viewModel.checkImage(image)
                    } else {
                        val index = state.images.indexOf(image)
                        viewModel.selectImage(index)
                        openPreview()
                    }
                }
            )
            if (state.isDeleteMode && state.checkedImages.isNotEmpty()) {
                DeleteDialog(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    checkedImagesCount = state.checkedImages.size,
                    onBackClicked = {
                        viewModel.changeDeleteMode()
                    },
                    onDeleteClicked = {
                        viewModel.deleteImages()
                        viewModel.changeDeleteMode()
                    }
                )
            }
        }
    }

}





