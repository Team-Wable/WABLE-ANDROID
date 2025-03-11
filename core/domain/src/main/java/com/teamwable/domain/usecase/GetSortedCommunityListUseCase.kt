package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class GetSortedCommunityListUseCase @Inject constructor(
    private val getJoinedCommunityNameUseCase: GetJoinedCommunityNameUseCase,
    private val getShuffledCommunityListUseCase: GetShuffledCommunityListUseCase,
) {
    operator fun invoke(): Flow<List<CommunityModel>> {
        return combine(
            getShuffledCommunityListUseCase(),
            getJoinedCommunityNameUseCase(),
        ) { communities, joinedCommunityName ->
            if (joinedCommunityName.isBlank()) return@combine communities

            val communityList = communities.toMutableList()
            val index = communityList.indexOfFirst { it.communityName == joinedCommunityName }

            if (index != NOT_FOUND) {
                val joinedCommunity = communityList.removeAt(index)
                communityList.add(FIRST_INDEX, joinedCommunity)
            }
            communityList
        }
    }

    companion object {
        private const val NOT_FOUND = -1
        private const val FIRST_INDEX = 0
    }
}
