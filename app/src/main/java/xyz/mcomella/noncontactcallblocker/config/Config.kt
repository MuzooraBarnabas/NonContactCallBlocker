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
