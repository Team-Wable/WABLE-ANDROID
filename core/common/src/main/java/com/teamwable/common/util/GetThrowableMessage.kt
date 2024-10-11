package com.teamwable.common.util

import android.content.res.Resources
import com.teamwable.common.R
import com.teamwable.model.network.Error
import java.net.UnknownHostException

fun getThrowableMessage(
    throwable: Throwable?, localContextResource: Resources,
): String = when (throwable) {
    is UnknownHostException -> localContextResource.getString(R.string.error_message_network)
    is Error.NetWorkConnectError -> localContextResource.getString(R.string.error_message_network)
    is Error.ApiError -> throwable.message.toString()
    is Error.TimeOutError -> throwable.message.toString()
    else -> localContextResource.getString(R.string.error_message_unknown)
}
