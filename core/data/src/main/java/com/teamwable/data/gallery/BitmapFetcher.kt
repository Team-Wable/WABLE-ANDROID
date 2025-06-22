package com.teamwable.data.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.teamwable.network.di.WithoutTokenInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class BitmapFetcher @Inject constructor(
    @WithoutTokenInterceptor private val okHttpClient: OkHttpClient,
) {
    suspend fun fetchBitmapFromUrl(url: String): Bitmap = withContext(Dispatchers.IO) {
        okHttpClient.newCall(Request.Builder().url(url).build())
            .execute()
            .use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Failed to download image: HTTP ${response.message}")
                }

                response.body?.byteStream().use { inputStream ->
                    BitmapFactory.decodeStream(inputStream)
                        ?: throw IOException("Failed to decode bitmap from stream")
                }
            }
    }
}
