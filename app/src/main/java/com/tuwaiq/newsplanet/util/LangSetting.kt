package com.tuwaiq.newsplanet.util

import android.content.Context
import android.content.res.Configuration
import java.util.*

class LangSetting( val context: Context) {
    fun setLocals(s: String) {
        val locale = Locale(s)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        context.resources?.updateConfiguration(config, context.resources.displayMetrics)
    }
}