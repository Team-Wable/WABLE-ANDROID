package com.teamwable.main.intentprovider

import android.content.Context
import android.content.Intent
import com.teamwable.common.intentprovider.IntentProvider
import com.teamwable.main.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class DefaultIntentProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : IntentProvider {
    override fun getIntent(): Intent =
        MainActivity.createIntent(context)
}
