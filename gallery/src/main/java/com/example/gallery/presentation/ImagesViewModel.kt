package com.example.gallery.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.models.Image
import com.example.gallery.domain.usecases.DeleteImageUseCase
import com.example.gallery.domain.usecases.GetImagesUseCase
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
class ImagesViewModel @Inject constructor(
    private val getImagesUseCase: GetImagesUseCase,
    private val deleteImageUseCase: DeleteImageUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(State())
    val state: StateFlow<State> = _state.asStateFlow()


    fun getImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val images = getImagesUseCase().take(10)
            withContext(Dispatchers.Main) {
                _state.update {
                    it.copy(images = images)
                }
            }
        }
    }

    fun deleteImages() {
        val checkedImages = state.value.checkedImages
        for (image in checkedImages) {
            deleteImageUseCase(image.path)
        }
        _state.update {
            it.copy(checkedImages = emptyList())
        }
        getImages()
    }

    fun changeEditMode() {
        _state.update {
            it.copy(isEditMode = state.value.isEditMode.not())
        }
    }

    fun checkImage(image: Image) {
        if (state.value.isChecked(image).not()) {
            val checkedImages = state.value.checkedImages.toMutableList()
            checkedImages.add(image)
            _state.update {
                it.copy(checkedImages = checkedImages)
            }
        } else {
            val uncheckedImages = state.value.checkedImages.toMutableList()
            uncheckedImages.remove(image)
            _state.update {
                it.copy(checkedImages = uncheckedImages)
            }
        }

    }

    fun selectImage(index: Int) {
        _state.update {
            it.copy(selectedImageIndex = index)
        }
    }

    fun clearSelected() {
        _state.update {
            it.copy(checkedImages = emptyList())
        }
    }

    fun changeDeleteMode() {
        _state.update {
            it.copy(isDeleteMode = state.value.isDeleteMode.not())
        }
    }

    fun zoom(increase: Boolean) {
        val coefficient = if (increase) 0.25F else -0.25F
        _state.update {
            it.copy(zoom = it.zoom + coefficient)
        }
    }


    data class State(
        val images: List<Image> = emptyList(),
        val checkedImages: List<Image> = emptyList(),
        val isEditMode: Boolean = false,
        val isDeleteMode: Boolean = false,
        val selectedImageIndex: Int = -1,
        val zoom: Float = 1f
    ) {

        fun isChecked(image: Image): Boolean {
            return checkedImages.contains(image)
        }

    }
}