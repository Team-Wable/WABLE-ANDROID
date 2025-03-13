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
        fakeCommunityRepository = FakeCommunityRepository(communities)
        useCase = GetSortedCommunityListUseCase(
            getShuffledCommunityListUseCase = GetShuffledCommunityListUseCase(fakeCommunityRepository),
            moveCommunityToTopUseCase = MoveCommunityToTopUseCase(),
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
        @Test
        @DisplayName("섞인 리스트는 원본 리스트와 동일한 요소를 가져야 한다")
        fun `shuffled list should contain the same elements as the original list`() = runTest {
            // When: 정렬된 커뮤니티 리스트를 조회한다
            val sortedList = useCase.invoke("false name").single().first
            // Then: 순서는 다르지만, 요소는 동일해야 한다
            assertEquals(communities.toSet(), sortedList.toSet())
        }
    }

    @Nested
    @DisplayName("Progress 관련 테스트")
    inner class ProgressTest {
        @Test
        @DisplayName("선택한 커뮤티니가 있는 경우 해당 커뮤니티의 progress를 반환 한다")
        fun `should return progress if selected community exists`() = runTest {
            // Given: "Community D"를 선택한 경우
            val preRegisterTeamName = "Community D"

            // When: 정렬된 커뮤니티 리스트를 조회
            val result = useCase.invoke(preRegisterTeamName).single()

            // Then: progress는 9f 이어야 한다
            assertEquals(9f, result.second, "Progress는 0이어야 한다.")
        }

        @Test
        @DisplayName("선택한 커뮤니티가 없을 때 progress는 0이어야 한다")
        fun `progress should be 0 when no selected community`() = runTest {
            // Given: 선택한 커뮤니티가 없을 경우 (preRegisterTeamName이 빈 문자열)
            val preRegisterTeamName = ""

            // When: 정렬된 커뮤니티 리스트를 조회
            val result = useCase.invoke(preRegisterTeamName).single()

            // Then: progress는 0이어야 한다
            assertEquals(0f, result.second, "Progress는 0이어야 한다.")
        }

        @Test
        @DisplayName("선택한 커뮤니티가 잘못 된 경우 progress는 0이어야 한다")
        fun `progress should be 0 when selected community not exist`() = runTest {
            // Given: 선택한 커뮤니티가 잘못 된 경우
            val preRegisterTeamName = "Non existent"

            // When: 정렬된 커뮤니티 리스트를 조회
            val result = useCase.invoke(preRegisterTeamName).single()

            // Then: progress는 0이어야 한다
            assertEquals(0f, result.second, "Progress는 0이어야 한다.")
        }

        @Nested
        @DisplayName("리스트가 비어 있는 경우")
        inner class EmptyCommunityListTest {
            @BeforeEach
            fun setUp() { // Given : 비어있는 리스트가 주어진다
                fakeCommunityRepository = FakeCommunityRepository(emptyList())
                useCase = GetSortedCommunityListUseCase(
                    getShuffledCommunityListUseCase = GetShuffledCommunityListUseCase(fakeCommunityRepository),
                    moveCommunityToTopUseCase = MoveCommunityToTopUseCase(),
                )
            }

            @Test
            @DisplayName("선택한 커뮤니티가 잘못 되고 리스트가 없는 경우 progress는 0이어야 한다")
            fun `progress should be 0 when selected community and list not exist`() = runTest {
                // Given: 선택한 커뮤니티가 잘못 된 경우
                val preRegisterTeamName = "non existent"

                // When: 정렬된 커뮤니티 리스트를 조회
                val result = useCase.invoke(preRegisterTeamName).single()

                // Then: progress는 0이어야 한다
                assertEquals(0f, result.second, "Progress는 0이어야 한다.")
            }

            @Test
            @DisplayName("리스트가 없는 경우 progress는 0이어야 한다")
            fun `progress should be 0 when list not exist`() = runTest {
                // Given: 리스트가 없는 경우
                val preRegisterTeamName = "Community D"

                // When: 정렬된 커뮤니티 리스트를 조회
                val result = useCase.invoke(preRegisterTeamName).single()

                // Then: progress는 0이어야 한다
                assertEquals(0f, result.second, "Progress는 0이어야 한다.")
            }
        }
    }
}
