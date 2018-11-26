/* Copyright (C) 2018 Michael Comella
 *
 * This file is part of NonContactCallBlocker.
 *
 *  NonContactCallBlocker is free software: you can redistribute it
 *  and/or modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation, either version 3 of
 *  the License, or (at your option) any later version.
 *
 *  NonContactCallBlocker is distributed in the hope that it will be
 *  useful, but WITHOUT ANY WARRANTY; without even the implied
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *  See the GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with NonContactCallBlocker.  If not, see
 *  <https://www.gnu.org/licenses/>. */

package xyz.mcomella.noncontactcallblocker.config

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import androidx.preference.PreferenceManager
import xyz.mcomella.noncontactcallblocker.R

private const val KEY_IS_INITIAL_PERMISSIONS_REQUEST_COMPLETE = "isInitialPermissionsRequestComplete"

class Config private constructor(
        val sharedPrefs: SharedPreferences,
        private val res: Resources
) {

    // The Preference class automatically adds values to shared prefs so we can't just use contain on keyIsBlockingEnabled.
    var isInitialPermissionsRequestComplete: Boolean
        get() = sharedPrefs.getBoolean(KEY_IS_INITIAL_PERMISSIONS_REQUEST_COMPLETE, false)
        set(value) = sharedPrefs.edit().putBoolean(KEY_IS_INITIAL_PERMISSIONS_REQUEST_COMPLETE, value).apply()

    private val keyIsBlockingEnabled = res.getString(R.string.key_global_enable)
    var isBlockingEnabled: Boolean
        get() = sharedPrefs.getBoolean(keyIsBlockingEnabled, res.getBoolean(R.bool.default_global_enable))
        set(value) = sharedPrefs.edit().putBoolean(keyIsBlockingEnabled, value).apply()

    companion object {
        fun create(context: Context): Config {
            return Config(PreferenceManager.getDefaultSharedPreferences(context), context.resources)
        }
    }
}
