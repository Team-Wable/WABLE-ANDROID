package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

@DisplayName("GetSortedCommunityListUseCase 테스트")
internal class MoveCommunityToTopUseCaseTest : BaseCommunityUseCaseTest() {
    private lateinit var moveCommunityToTopUseCase: MoveCommunityToTopUseCase

    @BeforeEach
    override fun setUp() {
        moveCommunityToTopUseCase = MoveCommunityToTopUseCase()
    }

    @Nested
    @DisplayName("선택한 팀이 리스트에 있을 때")
    inner class SelectedTeamExists {
        @Test
        @DisplayName("선택한 팀을 리스트 맨 앞으로 이동시켜야 한다")
        fun `should move the selected team to the top`() {
            // Given: "Community B"를 선택했을 때
            val selectedTeamName = "Community B"

            // When: 리스트에서 "Community B"를 맨 앞으로 이동시키면
            val result = moveCommunityToTopUseCase(communities, selectedTeamName)

            // When: "Community B"가 리스트 맨 앞에 있어야 한다
            assertEquals("Community B", result.first().communityName, "선택한 팀은 맨 앞에 정렬.")
            assertEquals(communities.size, result.size, "결과 리스트의 크기는 원본 리스트와 동일.")
        }
    }

    @Nested
    @DisplayName("선택한 팀이 리스트에 없을 때")
    inner class SelectedTeamEmpty {
        @Test
        @DisplayName("리스트의 순서는 변경되지 않아야 한다")
        fun `should not change the order of the list`() {
            // Given: 선택한 팀이 없을 때
            val selectedTeamName = ""

            // When: 리스트를 정렬 하면
            val result = moveCommunityToTopUseCase(communities, selectedTeamName)

            // 리스트가 변경되지 않아야 한다
            assertEquals(communities, result, "선택한 팀이 없으면 리스트가 그대로 유지되어야 합니다.")
        }
    }

    @Nested
    @DisplayName("선택한 팀의 이름이 다를 때")
    inner class SelectedTeamNotExists {
        @Test
        @DisplayName("리스트의 순서는 변경되지 않아야 한다")
        fun `should not change the order of the list`() {
            // Given: 선택한 팀이 없을 때
            val selectedTeamName = "Not Exists"

            // When: 리스트를 정렬 하면
            val result = moveCommunityToTopUseCase(communities, selectedTeamName)

            // 리스트가 변경되지 않아야 한다
            assertEquals(communities, result, "선택한 팀이 이름이 다르면 리스트가 그대로 유지되어야 합니다.")
        }
    }

    @Nested
    @DisplayName("리스트가 비어있을 때")
    inner class EmptyList {
        @Test
        @DisplayName("빈 리스트를 반환해야 한다")
        fun `should return an empty list when no communities exist`() {
            // Given: 리스트가 비어있을 때
            val communities = emptyList<CommunityModel>()
            val selectedTeamName = "Community A"

            // When: 리스트를 정렬하면
            val result = moveCommunityToTopUseCase(communities, selectedTeamName)

            // Then: 빈 리스트를 반환해야 한다
            assertTrue(result.isEmpty(), "리스트가 비어있으면 빈 리스트를 반환")
        }
    }

    @Nested
    @DisplayName("선택한 팀이 이미 맨 위에 있을 때")
    inner class SelectedTeamAtTop {
        @Test
        @DisplayName("리스트의 순서는 변경되지 않아야 한다")
        fun `should not modify the order if the selected team is already at the top`() {
            // Given: 선택한 팀이 이미 맨 위에 있을 때
            val selectedTeamName = "Community A"

            // When: 리스트를 정렬하면
            val result = moveCommunityToTopUseCase(communities, selectedTeamName)

            // Then: 리스트가 변경되지 않아야 한다
            assertEquals(communities, result, "선택한 팀이 이미 맨 위에 있으면 리스트가 변경되지 않아야 한다.")
        }
    }
}
