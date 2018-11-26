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

package xyz.mcomella.noncontactcallblocker.integration

import androidx.arch.core.executor.testing.CountingTaskExecutorRule
import androidx.room.Room
import androidx.test.InstrumentationRegistry
import org.junit.After
import org.junit.Before
import org.junit.Rule
import xyz.mcomella.noncontactcallblocker.db.AppDB
import java.util.concurrent.TimeUnit

/**
 * An integration test base class that initializes [AppDB] for testing.
 */
abstract class AppDBTest {

    @get:Rule val countingTaskRule = CountingTaskExecutorRule()

    lateinit var db: AppDB
        private set

    @Before
    fun setUpDB() {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), AppDB::class.java)
                .build()
    }

    @After
    fun tearDownDB() {
        countingTaskRule.drainTasks(10, TimeUnit.SECONDS)
        db.close()
    }
}
