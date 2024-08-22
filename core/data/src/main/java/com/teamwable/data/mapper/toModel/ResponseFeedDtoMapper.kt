package com.teamwable.data.mapper.toModel

import com.teamwable.model.Feed
import com.teamwable.network.dto.response.ResponseHomeFeedDto
import com.teamwable.network.dto.response.ResponseProfileFeedDto

internal fun ResponseHomeFeedDto.toFeed(): Feed =
    Feed(
        this.memberId,
        this.memberProfileUrl,
        this.memberNickname,
        this.contentId,
        this.contentTitle,
        this.contentText,
        this.time,
        this.isGhost,
        this.memberGhost,
        this.isLiked,
        this.likedNumber.toString(),
        this.commentNumber.toString(),
        this.contentImageUrl,
        this.memberFanTeam,
    )

// 서버랑 상의 후 나중에 사라질 함수인 듯 해서 일단 같은 파일에 만들어 놓음
internal fun ResponseProfileFeedDto.toFeed(): Feed =
    Feed(
        this.memberId,
        this.memberProfileUrl,
        this.memberNickname,
        this.contentId,
        this.contentTitle,
        this.contentText,
        this.time,
        this.isGhost,
        this.memberGhost,
        this.isLiked,
        this.likedNumber.toString(),
        this.commentNumber.toString(),
        "",
        this.memberFanTeam,
    )
