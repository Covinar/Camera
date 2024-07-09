package com.example.gallery.presentation.preview

import android.annotation.SuppressLint
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.common.models.Image
import com.example.gallery.R
import com.example.gallery.presentation.ImagesViewModel
import com.example.gallery.presentation.common.DeleteDialog

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun ImagePreview(
    viewModel: ImagesViewModel,
    openGallery: () -> Unit,
) {

    val state by viewModel.state.collectAsState()

    val image = state.images[state.selectedImageIndex]

    val horizontalPagerState = rememberPagerState(
        initialPage = state.selectedImageIndex
    ) {
        state.images.size
    }

    Scaffold(
        topBar = {
            TopBar(
                image = image,
                onDeleteClicked = {
                    viewModel.changeDeleteMode()
                },
                onCloseClicked = {
                    openGallery()
                }
            )
        }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            HorizontalPager(
                state = horizontalPagerState
            ) {
                GlideImage(
                    model = state.images[it].path,
                    contentDescription = null,
                    modifier = Modifier
                        .graphicsLayer(
                            scaleX = state.zoom,
                            scaleY = state.zoom
                        )
                )
            }

            ZoomImageBottomBar(
                percent = (state.zoom * 100).toInt(),
                onZoomClicked = { increase ->
                    viewModel.zoom(increase)
                },
                modifier = Modifier.align(Alignment.BottomCenter)
            )
            if (state.isDeleteMode) {
                DeleteDialog(
                    modifier = Modifier
                        .padding(it)
                        .align(Alignment.BottomCenter),
                    checkedImagesCount = 1,
                    onBackClicked = {
                        viewModel.changeDeleteMode()
                    },
                    onDeleteClicked = {
                        viewModel.checkImage(state.images[state.selectedImageIndex])
                        viewModel.deleteImages()
                        viewModel.changeDeleteMode()
                    }
                )
            }
        }
    }
}

@Composable
fun ZoomImageBottomBar(
    percent: Int,
    onZoomClicked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(Color.White)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_increase),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    onZoomClicked(true)
                }
        )
        Text(
            text = "$percent %",
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Icon(
            painter = painterResource(id = R.drawable.ic_decrease),
            contentDescription = null,
            modifier = Modifier
                .clickable {
                    onZoomClicked(false)
                }
        )
    }
    
}
