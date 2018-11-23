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

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import xyz.mcomella.noncontactcallblocker.db.BlockedCallDao
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import xyz.mcomella.noncontactcallblocker.ext.testValue
import java.util.*

class BlockedCallDaoTest : AppDBTest() {

    private lateinit var dao: BlockedCallDao

    @Before
    fun setUp() {
        dao = db.blockedCallDao()
    }

    @Test
    fun GIVEN_an_empty_db_THEN_the_blocked_calls_list_is_empty() {
        assertEquals(emptyList<List<BlockedCallEntity>>(), dao.loadBlockedCalls().testValue)
    }

    @Test
    fun WHEN_one_full_call_is_inserted_into_the_db_THEN_load_will_return_it() {
        val expected = listOf(BlockedCallEntity("5555555555", Date(1234567)))
        dao.insertBlockedCalls(*expected.toTypedArray())
        assertEquals(expected, dao.loadBlockedCalls().testValue)
    }

    @Test
    fun WHEN_one_incomplete_call_is_inserted_into_the_db_THEN_load_will_return_it() {
        val expected = listOf(BlockedCallEntity(null, Date(1234567)))
        dao.insertBlockedCalls(*expected.toTypedArray())
        assertEquals(expected, dao.loadBlockedCalls().testValue)
    }

    @Test
    fun WHEN_many_calls_inserted_into_db_THEN_load_will_return_them_by_descending_date() {
        val input = mapOf(
                "55566666666" to Date(7),
                "55555555555" to Date(2),
                "5551231234" to Date(3),
                "5559876543" to Date(1)
        ).map { (number, date) -> BlockedCallEntity(number, date) }.toTypedArray()
        dao.insertBlockedCalls(*input)

        val actual = dao.loadBlockedCalls().testValue
        val expected = actual.sortedByDescending { it.date }
        assertEquals(expected, actual)
    }
}
