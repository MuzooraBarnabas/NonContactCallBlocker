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

package xyz.mcomella.noncontactcallblocker

import android.app.Application
import android.os.StrictMode

open class CallBlockApplication : Application() {

    // Context is not complete until constructor returns so lazy.
    val serviceLocator by lazy { ServiceLocator(this) }

    override fun onCreate() {
        initStrictMode()
        super.onCreate()
    }

    private fun initStrictMode() {
        StrictMode.ThreadPolicy.Builder()
                .detectAll()
                .penaltyDeath()
                .build()
                .let { StrictMode.setThreadPolicy(it) }

        StrictMode.VmPolicy.Builder()
                .detectAll()
                .penaltyDeath()
                .build()
                .let { StrictMode.setVmPolicy(it) }
    }
}
