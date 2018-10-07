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

package xyz.mcomella.noncontactcallblocker.blocklist

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import xyz.mcomella.noncontactcallblocker.db.AppDB

class CallBlockListViewModel(blockedCallRepository: BlockedCallRepository) : ViewModel() {

    val blockedCalls: LiveData<List<BlockedCallEntity>> = blockedCallRepository.getBlockedCalls()

    class Factory : ViewModelProvider.NewInstanceFactory() {
        @Suppress("UNCHECKED_CAST") // Need to return modelClass type.
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = when (modelClass) {
            CallBlockListViewModel::class.java -> {
                val repository = BlockedCallRepository(AppDB.db) // todo: to singleton.
                CallBlockListViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown modelClass $modelClass")
        }
    }
}
