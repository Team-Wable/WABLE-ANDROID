package com.teamwable.data.repositoryimpl

import com.teamwable.data.mapper.toModel.toDummy
import com.teamwable.data.remote.datasource.DummyService
import com.teamwable.data.repository.DummyRepository
import com.teamwable.data.util.EndPoints.ERROR
import com.teamwable.model.Dummy
import java.net.HttpURLConnection
import javax.inject.Inject

class DefaultDummyRepository @Inject constructor(
    private val dummyService: DummyService
) : DummyRepository {
    override suspend fun getDummy(): Result<List<Dummy>> {
        return runCatching {
            val response = dummyService.getDummy()
            if (response.status == HttpURLConnection.HTTP_OK) {
                response.data?.map { it.toDummy() } ?: emptyList()
            } else {
                throw Exception("${ERROR}${response.status}")
            }
        }
    }
}