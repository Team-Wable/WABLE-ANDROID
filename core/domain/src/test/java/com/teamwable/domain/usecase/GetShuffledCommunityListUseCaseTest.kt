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
internal class GetShuffledCommunityListUseCaseTest : BaseCommunityUseCaseTest() {
    private lateinit var useCase: GetShuffledCommunityListUseCase

    @BeforeEach
    override fun setUp() {
        fakeCommunityRepository = FakeCommunityRepository(communities, "")
        useCase = GetShuffledCommunityListUseCase(fakeCommunityRepository)
    }

    @Nested
    @DisplayName("커뮤니티 리스트 셔플 테스트")
    inner class ShuffleCommunityListTest {
        private lateinit var shuffledList: List<CommunityModel>

        @BeforeEach
        fun setUpShuffledList() = runTest {
            // When: 커뮤니티 리스트가 셔플이 되면
            shuffledList = useCase().single()
            printOriginAndShuffledList(shuffledList)
        }

        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 크기가 동일해야 한다")
        fun `shuffled list should have the same size as the original list`() {
            // Then: 크기 비교
            assertEquals(communities.size, shuffledList.size)
        }

        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 순서가 달라야 한다")
        fun `shuffled list should have a different order than the original list`() {
            // Then: 순서 비교
            assertNotEquals(communities, shuffledList)
        }
    }
}
