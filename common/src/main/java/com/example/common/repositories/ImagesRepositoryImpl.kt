package com.example.common.repositories

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.common.models.Image
import java.io.File
import java.io.OutputStream


class ImagesRepositoryImpl(
    private val context: Context
) : ImagesRepository {

    override fun saveImage(bitmap: Bitmap) {
        val filename = "IMG_${System.currentTimeMillis()}.jpg"
        var fos: OutputStream?
        val imageUri: Uri?
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/Camera")
            put(MediaStore.Video.Media.IS_PENDING, 1)
        }
        val contentResolver = context.contentResolver
        imageUri =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
        imageUri?.let { uri ->
            fos = contentResolver.openOutputStream(uri)
            fos?.use { bitmap.compress(Bitmap.CompressFormat.JPEG, 70, it) }
            contentValues.clear()
            contentValues.put(MediaStore.Video.Media.IS_PENDING, 0)
            contentResolver.update(uri, contentValues, null, null)
        }
    }

    override fun deleteImage(path:String) {
        File(path).delete()
    }

    override fun getImages(): List<Image> {
        val contentResolver = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null
        )
        val images: MutableList<Image> = mutableListOf()
        while (contentResolver?.moveToNext() == true) {
            val name =
                contentResolver.getString(contentResolver.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
            val path =
                contentResolver.getString(contentResolver.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
            val date =
                contentResolver.getLong(contentResolver.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED))
            val id =
                contentResolver.getLong(contentResolver.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val image = Image(name, path, date)
            images.add(image)
        }
        contentResolver?.close()
        return images.sortedByDescending { it.date }
    }
}