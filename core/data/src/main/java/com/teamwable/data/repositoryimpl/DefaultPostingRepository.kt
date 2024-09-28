package com.teamwable.data.repositoryimpl

import android.content.ContentResolver
import com.teamwable.data.repository.PostingRepository
import com.teamwable.data.util.createImagePart
import com.teamwable.network.datasource.PostingService
import com.teamwable.network.util.handleThrowable
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import javax.inject.Inject

internal class DefaultPostingRepository @Inject constructor(
    private val contentResolver: ContentResolver,
    private val postingService: PostingService,
) : PostingRepository {
    override suspend fun postingMultiPart(title: String, content: String, uriImage: String?): Result<Unit> {
        return runCatching {
            val textRequestBody = createContentRequestBody(title, content)
            val imagePart = contentResolver.createImagePart(uriImage, FILE_NAME)

            postingService.postingMultiPart(textRequestBody, imagePart)
            Unit
        }.onFailure { return it.handleThrowable() }
    }

    private fun createContentRequestBody(title: String, content: String): RequestBody {
        val contentJson = JSONObject().apply {
            put("contentTitle", title)
            put("contentText", content)
        }.toString()
        return contentJson.toRequestBody("application/json".toMediaTypeOrNull())
    }

    companion object {
        private const val FILE_NAME = "image"
    }
}
