package com.teamwable.navigation

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

object CustomNavType {
    inline fun <reified T> createNavType(): NavType<T> = object : NavType<T>(
        isNullableAllowed = false,
    ) {
        override fun get(bundle: Bundle, key: String): T? {
            val jsonString = bundle.getString(key) ?: return null
            return Json.decodeFromString(serializer(), jsonString)
        }

        override fun parseValue(value: String): T {
            return Json.decodeFromString(serializer(), Uri.decode(value))
        }

        override fun serializeAsValue(value: T): String {
            return Uri.encode(Json.encodeToString(serializer(), value))
        }

        override fun put(bundle: Bundle, key: String, value: T) {
            bundle.putString(key, Json.encodeToString(serializer(), value))
        }
    }
}
