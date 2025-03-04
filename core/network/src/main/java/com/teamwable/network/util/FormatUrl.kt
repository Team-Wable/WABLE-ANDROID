package com.teamwable.network.util

fun formatUrl(url: String): String {
    return if (!url.startsWith("http://") && !url.startsWith("https://")) {
        "http://$url"
    } else {
        url
    }
}
