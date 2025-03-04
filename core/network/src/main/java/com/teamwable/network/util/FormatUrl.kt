package com.teamwable.network.util

import timber.log.Timber

fun formatUrl(url: String): String {
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        Timber.e("https://$url/")
        "https://$url"
    } else {
        url
    }
}
