package com.teamwable.data.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class BitmapFetcher @Inject constructor(
    private val okHttpClient: OkHttpClient,
) {
    suspend fun fetchBitmapFromUrl(url: String): Bitmap = withContext(Dispatchers.IO) {
        val request = Request.Builder().url(url).build()

        try {
            val response = okHttpClient.newCall(request).execute()

            if (!response.isSuccessful) {
                response.close()
                throw IOException("Failed to download image: HTTP ${response.message}")
            }

            response.body?.byteStream()?.use { inputStream ->
                BitmapFactory.decodeStream(inputStream)
                    ?: throw IOException("Failed to decode bitmap from stream")
            } ?: throw IOException("Response body is null")
        } catch (e: Exception) {
            throw IOException("Failed to fetch image bitmap", e)
        }
    }
}
