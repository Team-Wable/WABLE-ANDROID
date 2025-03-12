package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import org.junit.jupiter.api.BeforeEach

internal abstract class BaseCommunityUseCaseTest {
    protected lateinit var fakeCommunityRepository: FakeCommunityRepository

    @BeforeEach
    abstract fun setUp()

    protected fun printOriginAndShuffledList(shuffledList: List<CommunityModel>) {
        println("원본 리스트:")
        communities.forEachIndexed { index, community ->
            println("[$index] $community")
        }
        println("\n정렬 후 리스트:")
        shuffledList.forEachIndexed { index, community ->
            println("[$index] $community")
        }
        println()
    }

    companion object {
        val communities = listOf(
            CommunityModel("Community A", 1f),
            CommunityModel("Community B", 3f),
            CommunityModel("Community C", 0f),
            CommunityModel("Community D", 9f),
            CommunityModel("Community E", 8f),
            CommunityModel("Community F", 2f),
            CommunityModel("Community G", 10f),
        )
    }
}
