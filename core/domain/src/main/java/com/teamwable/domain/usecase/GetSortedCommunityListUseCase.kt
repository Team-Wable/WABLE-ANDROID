package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSortedCommunityListUseCase @Inject constructor(
    private val getShuffledCommunityListUseCase: GetShuffledCommunityListUseCase,
) {
    operator fun invoke(preRegisterTeamName: String): Flow<Pair<List<CommunityModel>, Float>> {
        return getShuffledCommunityListUseCase().map { communities ->
            if (preRegisterTeamName.isBlank()) return@map communities to 0f

            val communityList = communities.toMutableList()
            val index = communityList.indexOfFirst { it.communityName == preRegisterTeamName }
            var progress = 0f

            if (index != NOT_FOUND) {
                progress = communityList[index].communityNum
                val joinedCommunity = communityList.removeAt(index)
                communityList.add(FIRST_INDEX, joinedCommunity)
            }
            communityList to progress
        }
    }

    companion object {
        private const val NOT_FOUND = -1
        private const val FIRST_INDEX = 0
    }
}
