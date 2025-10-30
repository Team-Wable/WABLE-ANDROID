package com.teamwable.data.gallery

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import com.teamwable.data.util.calculateInSampleSize
import com.teamwable.data.util.rotateBitmap
import com.teamwable.network.di.WithoutTokenInterceptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.BufferedInputStream
import java.io.IOException
import javax.inject.Inject

internal class BitmapFetcher @Inject constructor(
    @WithoutTokenInterceptor private val okHttpClient: OkHttpClient,
) {
    suspend fun fetchBitmapFromUrl(url: String): Bitmap = withContext(Dispatchers.IO) {
        okHttpClient.newCall(Request.Builder().url(url).build())
            .execute()
            .use { response ->
                if (!response.isSuccessful) {
                    throw IOException("Failed to download image: HTTP ${response.message}")
                }

                val inputStream = response.body?.byteStream()
                    ?: throw IOException("Empty body while downloading image")

                BufferedInputStream(inputStream).use { bufferedStream ->
                    bufferedStream.mark(Int.MAX_VALUE)

                    val exif = ExifInterface(bufferedStream)
                    bufferedStream.reset()

                    val boundsOptions = BitmapFactory.Options().apply { inJustDecodeBounds = true }
                    BitmapFactory.decodeStream(bufferedStream, null, boundsOptions)
                    bufferedStream.reset()

                    val decodeOptions = BitmapFactory.Options().apply {
                        inSampleSize = calculateInSampleSize(boundsOptions, 1024, 1024)
                    }
                    val originalBitmap = BitmapFactory.decodeStream(bufferedStream, null, decodeOptions)
                        ?: throw IOException("Failed to decode bitmap")

                    val orientation = exif.getAttributeInt(
                        ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL,
                    )

                    return@use when (orientation) {
                        ExifInterface.ORIENTATION_ROTATE_90 -> rotateBitmap(originalBitmap, 90f)
                        ExifInterface.ORIENTATION_ROTATE_180 -> rotateBitmap(originalBitmap, 180f)
                        ExifInterface.ORIENTATION_ROTATE_270 -> rotateBitmap(originalBitmap, 270f)
                        else -> originalBitmap
                    }
                }
            }
    }
}
