package com.teamwable.ui.extensions

import android.content.Context
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.NavOptions
import com.teamwable.ui.R

sealed class DeepLinkDestination(val addressRes: Int) {
    data object TwoButtonDialog : DeepLinkDestination(R.string.deeplink_url_two_button_dialog)

    data object BottomSheet : DeepLinkDestination(R.string.deeplink_url_bottomsheet)

    data object TwoLabelBottomSheet : DeepLinkDestination(R.string.deeplink_url_two_label_bottomsheet)

    data object Posting : DeepLinkDestination(R.string.deeplink_url_posting)

    data object Profile : DeepLinkDestination(R.string.deeplink_url_profile)

    data object FeedImageDialog : DeepLinkDestination(R.string.deeplink_url_image_dialog)

    data object HomeDetail : DeepLinkDestination(R.string.deeplink_url_home_detail)

    data object Notification : DeepLinkDestination(R.string.deeplink_url_notification)
}

private fun DeepLinkDestination.getDeepLink(context: Context, args: Map<String, Any>?): String {
    var baseUri = context.getString(this.addressRes)

    args?.forEach { (key, value) ->
        baseUri = baseUri.replace("{$key}", value.toString())
    }

    return baseUri
}

fun NavController.deepLinkNavigateTo(
    context: Context,
    deepLinkDestination: DeepLinkDestination,
    arguments: Map<String, Any>? = null,
) {
    val builder = NavOptions.Builder()

    navigate(
        buildDeepLink(context, deepLinkDestination, arguments),
        builder.build(),
    )
}

private fun buildDeepLink(context: Context, destination: DeepLinkDestination, arguments: Map<String, Any>?) =
    NavDeepLinkRequest.Builder
        .fromUri(destination.getDeepLink(context, arguments).toUri())
        .build()
