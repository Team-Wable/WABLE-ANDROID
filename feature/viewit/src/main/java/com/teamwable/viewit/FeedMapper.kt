package com.teamwable.viewit

import com.teamwable.model.home.Feed
import com.teamwable.model.viewit.ViewIt

object FeedMapper {
    fun ViewIt.toFeed(): Feed {
        return Feed(
            postAuthorId = this.postAuthorId,
            postAuthorNickname = this.postAuthorNickname,
            title = this.linkTitle,
            content = this.viewItContent,
            feedId = viewItId,
        )
    }
}
