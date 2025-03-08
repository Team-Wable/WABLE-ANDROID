package com.teamwable.data.util

import com.teamwable.data.repositoryimpl.DefaultViewItRepository.Companion.DEFAULT_VIEW_IT
import com.teamwable.model.viewit.LinkInfo
import com.teamwable.network.util.formatUrl
import org.jsoup.Jsoup
import java.net.URL

class JsoupParser {
    companion object {
        private const val META_OG_IMAGE = "meta[property=og:image]"
        private const val META_OG_TITLE = "meta[property=og:title]"
        private const val META_OG_SITE_NAME = "meta[property=og:site_name]"

        fun parseMetadata(link: String): LinkInfo {
            val formattedUrl = formatUrl(link)
            val document = Jsoup.connect(formattedUrl).get()

            val imageUrl = document.select(META_OG_IMAGE).attr("content").ifEmpty { DEFAULT_VIEW_IT }
            val title = document.select(META_OG_TITLE).attr("content").ifEmpty { document.title() }
            val linkName = document.select(META_OG_SITE_NAME).attr("content").ifEmpty { URL(link).host }

            return LinkInfo(imageUrl, link, title, "", linkName)
        }
    }
}
