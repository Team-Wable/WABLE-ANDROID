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
    /**
     * Downloads an image from the specified URL and decodes it into a [Bitmap].
     *
     * Performs the network operation on the IO dispatcher. Throws an [IOException] if the HTTP request fails
     * or if the image cannot be decoded into a bitmap.
     *
     * @param url The URL of the image to download.
     * @return The decoded [Bitmap] object.
     * @throws IOException If the HTTP request is unsuccessful or the image cannot be decoded.
     */
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
