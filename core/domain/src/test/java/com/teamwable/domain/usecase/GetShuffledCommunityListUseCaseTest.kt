package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("GetShuffledCommunityListUseCase 테스트")
internal class GetShuffledCommunityListUseCaseTest {
    // 공통 테스트 픽스처
    private lateinit var useCase: GetShuffledCommunityListUseCase
    private lateinit var fakeCommunityRepository: FakeCommunityRepository

    // Given: 테스트에 사용할 커뮤니티 데이터
    private val communities = listOf(
        CommunityModel("Community A", 1f),
        CommunityModel("Community B", 3f),
        CommunityModel("Community C", 0f),
        CommunityModel("Community D", 9f),
        CommunityModel("Community E", 8f),
        CommunityModel("Community F", 2f),
        CommunityModel("Community G", 10f),
    )

    @BeforeEach
    fun setUp() {
        fakeCommunityRepository = FakeCommunityRepository(communities, "")
        useCase = GetShuffledCommunityListUseCase(fakeCommunityRepository)
    }

    @Nested
    @DisplayName("커뮤니티 리스트 랜덤 호출 테스트")
    inner class ShuffleCommunityListTest {
        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 크기가 동일해야 한다")
        fun `shuffled list should have the same size as the original list`() = runTest {
            // When: UseCase 실행
            val shuffledList = useCase().single()

            // Then: 크기 비교
            assertEquals(communities.size, shuffledList.size)
        }

        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 순서가 달라야 한다")
        fun `shuffled list should have a different order than the original list`() = runTest {
            // When: UseCase 실행
            val shuffledList = useCase().single()

            // Then: 순서 비교
            assertNotEquals(communities, shuffledList)
        }
    }
}
