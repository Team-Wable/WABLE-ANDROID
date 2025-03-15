package com.teamwable.domain.usecase

import com.teamwable.model.community.CommunityModel
import javax.inject.Inject

class MoveCommunityToTopUseCase @Inject constructor() {
    operator fun invoke(
        communities: List<CommunityModel>,
        selectedTeamName: String,
    ): List<CommunityModel> {
        val communityList = communities.toMutableList()

        val index = communityList.indexOfFirst { it.communityName == selectedTeamName }
        if (index != NOT_FOUND) {
            val joinedCommunity = communityList.removeAt(index)
            communityList.add(0, joinedCommunity)
        }

        return communityList
    }

    companion object {
        private const val NOT_FOUND = -1
    }
}
