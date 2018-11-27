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

package xyz.mcomella.noncontactcallblocker.repository

import androidx.lifecycle.LiveData
import kotlinx.coroutines.experimental.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import xyz.mcomella.noncontactcallblocker.db.BlockedCallDao
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import java.util.Date

class BlockedCallRepositoryTest {

    private lateinit var repository: BlockedCallRepository
    private lateinit var dao: BlockedCallDao

    @Before
    fun setUp() {
        dao = mock(BlockedCallDao::class.java)
        repository = BlockedCallRepository(dao)
    }

    @Test // A better test would test the inputs/outputs are the same but this is good enough for now.
    fun `WHEN loading blocked calls THEN the value directly from the DB is returned`() {
        @Suppress("UNCHECKED_CAST") // Can't do generics in mock method, I think.
        val expected = mock(LiveData::class.java) as LiveData<List<BlockedCallEntity>>
        `when`(dao.loadBlockedCalls()).thenReturn(expected)
        assertEquals(expected, repository.getBlockedCalls())
    }

    @Test
    fun `WHEN a repository receives a blocked call THEN the DAO tries to insert it`() = runBlocking {
        val expectedNumber = "55555555555"
        val expectedDate = Date(1234567)
        val expectedEntity = BlockedCallEntity(expectedNumber, expectedDate)

        repository.onCallBlocked(expectedNumber, expectedDate)
        verify(dao, times(1)).insertBlockedCalls(expectedEntity)
    }
}
