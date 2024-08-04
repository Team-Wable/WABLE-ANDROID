package com.teamwable.data.repository

import com.teamwable.model.Dummy

interface DummyRepository {
    suspend fun getDummy(): Result<List<Dummy>>
}