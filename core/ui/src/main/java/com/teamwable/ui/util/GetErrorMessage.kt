package com.teamwable.ui.util

import android.content.res.Resources
import com.teamwable.model.network.Error
import java.net.UnknownHostException

fun getErrorMessage(
    throwable: Throwable?,
    localContextResource: Resources,
) = when (throwable) {
    is UnknownHostException -> localContextResource.getString(com.teamwable.common.R.string.error_message_network)
    is Error.NetWorkConnectError -> localContextResource.getString(com.teamwable.common.R.string.error_message_network)
    is Error.ApiError -> throwable.message.toString()
    is Error.TimeOutError -> throwable.message.toString()
    else -> localContextResource.getString(com.teamwable.common.R.string.error_message_unknown)
}
