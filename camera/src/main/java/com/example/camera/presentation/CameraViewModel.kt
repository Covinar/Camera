package com.example.camera.presentation

import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.camera.domain.usecases.SaveImageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val saveImageUseCase: SaveImageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()

    fun changeFlashMode() {
        _state.update {
            it.copy(isFlashMode = it.isFlashMode.not())
        }
    }

    fun updateBitmap(bitmap: Bitmap) {
        val rotatedBitmap = rotateBitmap(bitmap = bitmap)
        _state.update {
            it.copy(bitmap = rotatedBitmap)
        }
    }

    fun resetBitmap() {
        _state.update {
            it.copy(bitmap = null)
        }
    }

    fun saveImage() {
        viewModelScope.launch(Dispatchers.IO) {
            state.value.bitmap?.let { saveImageUseCase(it) }
            withContext(Dispatchers.Main) {
                resetBitmap()
            }
        }
    }

    private fun rotateBitmap(bitmap: Bitmap) : Bitmap {
        val matrix = Matrix().apply { postRotate(90F) }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    data class State(
        val isFlashMode: Boolean = false,
        val bitmap: Bitmap? = null
    )

}