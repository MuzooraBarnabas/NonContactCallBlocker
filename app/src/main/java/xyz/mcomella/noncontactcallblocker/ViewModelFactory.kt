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

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import xyz.mcomella.noncontactcallblocker.ui.blocklist.CallBlockListViewModel

/**
 * A factory for view models created in the call block app.
 */
class ViewModelFactory(private val app: CallBlockApplication) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST") // Required for types
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when (modelClass) {
            CallBlockListViewModel::class.java -> CallBlockListViewModel(app.blockedCallRepository)
            else -> throw IllegalArgumentException("Unknown view model class $modelClass")
        } as T
    }
}
