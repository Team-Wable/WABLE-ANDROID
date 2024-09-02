package com.teamwable.ui.util

import android.annotation.SuppressLint
import android.content.Context
import com.teamwable.model.Feed
import com.teamwable.model.GhostColor
import com.teamwable.ui.extensions.stringOf
import com.teamwable.ui.R as r

object FeedTransformer {
    private val time = CalculateTime()
    private val transparent = Transparent()

    @SuppressLint("NewApi")
    fun handleFeedsData(feed: Feed, context: Context): Feed {
        val transformedUploadTime = context.getString(r.string.label_feed_upload_time, time.getCalculateTime(context, feed.uploadTime))
        val transformedGhost = if (feed.isPostAuthorGhost) context.stringOf(r.string.label_ghost_complete) else context.getString(r.string.label_feed_ghost_level, feed.postAuthorGhost)
        val transformedGhostColor = if (feed.isPostAuthorGhost) GhostColor.MINUS_85 else transparent.calculateColorWithOpacity(feed.postAuthorGhost.toInt())
        return feed.copy(uploadTime = transformedUploadTime, postAuthorGhost = transformedGhost, ghostColor = transformedGhostColor)
    }
}
