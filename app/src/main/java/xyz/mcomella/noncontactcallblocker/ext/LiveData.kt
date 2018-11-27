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

package xyz.mcomella.noncontactcallblocker.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.annotation.VisibleForTesting
import androidx.annotation.VisibleForTesting.NONE
import com.android.example.github.util.LiveDataTestUtil

fun <T, R> LiveData<T>.map(mapFun: (v: T) -> R): LiveData<R> {
    return Transformations.map(this, mapFun)
}

/**
 * Waits for the [LiveData.getValue] to be updated and returns the result: intended to replace
 * [LiveData.getValue] during tests.
 *
 * This is placed in production code to share it with test and androidTest.
 */
@VisibleForTesting(otherwise = NONE) val <T> LiveData<T>.testValue: T
    get() = LiveDataTestUtil.getValue(this)
