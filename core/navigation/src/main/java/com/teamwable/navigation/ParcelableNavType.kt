package com.teamwable.navigation

import android.net.Uri
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T : Parcelable> parcelableNavType(): NavType<T> = object : NavType<T>(
    isNullableAllowed = false,
) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.parcelable(key)

    override fun parseValue(value: String): T =
        Json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T): String =
        Uri.encode(Json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putParcelable(key, value)
    }
}

inline fun <reified T : Parcelable> Bundle.parcelable(key: String): T? = when {
    SDK_INT >= 33 -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION")
    getParcelable(key) as? T
}
