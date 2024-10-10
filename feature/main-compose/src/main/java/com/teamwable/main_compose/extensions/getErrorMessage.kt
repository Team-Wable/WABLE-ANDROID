package com.teamwable.main_compose.extensions

import android.content.res.Resources
import com.teamwable.main_compose.R
import com.teamwable.model.network.Error
import java.net.UnknownHostException

fun getErrorMessage(
    throwable: Throwable?,
    localContextResource: Resources,
) = when (throwable) {
    is UnknownHostException -> localContextResource.getString(R.string.error_message_network)
    is Error.NetWorkConnectError -> localContextResource.getString(R.string.error_message_network)
    is Error.ApiError -> throwable.message.toString()
    is Error.TimeOutError -> throwable.message.toString()
    else -> localContextResource.getString(R.string.error_message_unknown)
}
