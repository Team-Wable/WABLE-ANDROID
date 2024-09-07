package com.teamwable.network.util

import android.content.ContentResolver
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

suspend fun ContentResolver.createImagePart(uriString: String?, fileName: String): MultipartBody.Part? {
    val uri = runCatching { Uri.parse(uriString ?: return null) }
        .getOrNull()
        ?.takeUnless {
            it.scheme.equals("http", ignoreCase = true) || it.scheme.equals("https", ignoreCase = true)
        } ?: return null

    return withContext(Dispatchers.IO) {
        val imageRequestBody = ContentUriRequestBody(this@createImagePart, uri)
        imageRequestBody.toMultiPartData(fileName)
    }
}
