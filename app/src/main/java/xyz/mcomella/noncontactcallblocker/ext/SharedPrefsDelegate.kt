/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.noncontactcallblocker.ext

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

private val SUPPORTED_TYPES = arrayOf(
    Boolean::class.javaObjectType
)

/**
 * TODO
 */
class SharedPrefsDelegate<T : Any>(
        private val sharedPrefs: SharedPreferences,
        private val key: String,
        private val defaultValue: T
) {

    init {
        SUPPORTED_TYPES.firstOrNull { supportedType -> defaultValue::class == supportedType::class }
                ?: throw IllegalArgumentException("Unknown type")
    }

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = when (defaultValue) {
        is Boolean -> sharedPrefs.getBoolean(key, defaultValue)
    }
}
