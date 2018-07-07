package xyz.mcomella.noncontactcallblocker.config

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.support.v7.preference.PreferenceManager
import xyz.mcomella.noncontactcallblocker.R

class Config private constructor(
        private val sharedPrefs: SharedPreferences,
        private val res: Resources
) {

    val isBlockingEnabled: Boolean get() = sharedPrefs.getBoolean(res.getString(R.string.key_global_enable),
            res.getBoolean(R.bool.default_global_enable))

    companion object {
        private lateinit var singleton: Config
        fun get() = singleton

        fun init(context: Context) {
            singleton = Config(PreferenceManager.getDefaultSharedPreferences(context), context.resources)
        }
    }
}
