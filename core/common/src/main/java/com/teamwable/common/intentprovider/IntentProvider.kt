package com.teamwable.common.intentprovider

import android.content.Intent

interface IntentProvider {
    fun getIntent(): Intent
}
