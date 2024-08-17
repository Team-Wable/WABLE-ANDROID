package com.teamwable.network.util

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

suspend fun ContentResolver.createImagePart(uriString: String?): MultipartBody.Part? {
    if (uriString.isNullOrEmpty()) return null
    val uri = runCatching { Uri.parse(uriString) }.getOrElse { return null }

    return withContext(Dispatchers.IO) {
        val imageRequestBody = ContentUriRequestBody(this@createImagePart, uri)
        imageRequestBody.toMultiPartData("image")
    }
}
