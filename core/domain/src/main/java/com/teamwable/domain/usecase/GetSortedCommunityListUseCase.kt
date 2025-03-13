package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSortedCommunityListUseCase @Inject constructor(
    private val getShuffledCommunityListUseCase: GetShuffledCommunityListUseCase,
    private val moveCommunityToTopUseCase: MoveCommunityToTopUseCase,
) {
    operator fun invoke(preRegisterTeamName: String): Flow<Pair<List<CommunityModel>, Float>> {
        return getShuffledCommunityListUseCase().map { communities ->
            if (preRegisterTeamName.isBlank()) return@map communities to 0f
            val sortedList = moveCommunityToTopUseCase(communities, preRegisterTeamName)

            val progress =
                if (sortedList.isNotEmpty() && sortedList[0].communityName == preRegisterTeamName)
                    sortedList[0].communityNum
                else 0f
            sortedList to progress
        }
    }
}
