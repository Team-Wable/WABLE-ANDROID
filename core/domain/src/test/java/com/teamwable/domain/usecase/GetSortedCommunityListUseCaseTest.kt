package com.teamwable.domain.usecase

import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("GetSortedCommunityListUseCase 테스트")
internal class GetSortedCommunityListUseCaseTest : BaseCommunityUseCaseTest() {
    private lateinit var useCase: GetSortedCommunityListUseCase

    @BeforeEach
    override fun setUp() { // given : 커뮤니티 리스트와 가입된 커뮤니티 이름이 주어진다
        fakeCommunityRepository = FakeCommunityRepository(communities, "Community D")
        useCase = GetSortedCommunityListUseCase(
            getShuffledCommunityListUseCase = GetShuffledCommunityListUseCase(fakeCommunityRepository),
        )
    }

    @Nested
    @DisplayName("커뮤니티 리스트 정렬 테스트")
    inner class SortCommunityListTest {
        @Test
        @DisplayName("정렬된 리스트는 Community D가 최상단에 위치해야 한다")
        fun `sorted list should place joined community at the top`() = runTest {
            // When: "Community D"를 최상단으로 정렬한다
            val sortedList = useCase.invoke(preRegisterTeamName = "Community D").single().first
            printOriginAndShuffledList(sortedList)
            // Then: 최상단에 "Community D"가 정렬된다
            assertEquals("Community D", sortedList.first().communityName)
        }
    }

    @Nested
    @DisplayName("가입된 커뮤니티가 없는 경우 테스트")
    inner class NoJoinedCommunityTest {
        @BeforeEach
        fun setUp() { // Given : 가입된 커뮤니티가 없다면
            fakeCommunityRepository = FakeCommunityRepository(communities, "")
        }

        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 동일한 요소를 가져야 한다")
        fun `shuffled list should contain the same elements as the original list`() = runTest {
            // When: 정렬된 커뮤니티 리스트를 조회한다
            val sortedList = useCase.invoke("").single().first
            // Then: 순서는 다르지만, 요소는 동일해야 한다
            assertEquals(communities.toSet(), sortedList.toSet())
        }
    }

    @Nested
    @DisplayName("입력된 커뮤니티가 목록에 없는 경우 테스트")
    inner class InvalidJoinedCommunityTest {
        @BeforeEach
        fun setUp() { // Given : 존재하지 않는 커뮤니티 이름을 입력받으면
            fakeCommunityRepository = FakeCommunityRepository(communities, "존재 안함")
        }

        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 동일한 요소를 가져야 한다")
        fun `shuffled list should contain the same elements as the original list`() = runTest {
            // When: 정렬된 커뮤니티 리스트를 조회한다
            val sortedList = useCase.invoke("false name").single().first
            // Then: 순서는 다르지만, 요소는 동일해야 한다
            assertEquals(communities.toSet(), sortedList.toSet())
        }
    }
}
