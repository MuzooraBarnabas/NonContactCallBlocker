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

package xyz.mcomella.noncontactcallblocker.ui.blocklist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.robolectric.RobolectricTestRunner
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import xyz.mcomella.noncontactcallblocker.ext.testValue
import xyz.mcomella.noncontactcallblocker.repository.BlockedCallRepository
import java.util.*
import java.util.concurrent.TimeUnit

@RunWith(RobolectricTestRunner::class)
class CallBlockListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CallBlockListViewModel
    private lateinit var repoBlockedCalls: MutableLiveData<List<BlockedCallEntity>>

    @Before
    fun setUp() {
        repoBlockedCalls = MutableLiveData()
        val repository = mock(BlockedCallRepository::class.java).also {
            `when`(it.getBlockedCalls()).thenReturn(repoBlockedCalls)
        }
        viewModel = CallBlockListViewModel(repository)
    }

    @Test
    fun `WHEN an empty list is received THEN an empty list is returned`() {
        repoBlockedCalls.postValue(listOf())
        assertEquals(emptyList<List<BlockedCallEntity>>(), viewModel.blockedCalls.testValue)
    }

    @Test
    fun `WHEN a list of entities are received THEN they are converted to UI objects and returned`() {
        val calendar = GregorianCalendar(TimeZone.getTimeZone("UTC")).apply {
            clear()
            set(2018, 8, 4, 10, 37) // month is 0-index.
        }

        val input = listOf(
                BlockedCallEntity("5555555555", calendar.time),
                BlockedCallEntity(null, Date(calendar.timeInMillis + TimeUnit.HOURS.toMillis(12)))
        )

        repoBlockedCalls.postValue(input)
        val actual = viewModel.blockedCalls.testValue
        assertEquals(2, actual.size)

        // Entity conversion is tested elsewhere so we just sanity check this.
        val firstActual = actual.first()
        assertNotNull(firstActual.number)

        val message = "actual: $firstActual"
        assertTrue(message, firstActual.number!!.contains("555"))
        assertTrue(message, firstActual.date.contains("18")) // year: (20)18
        assertTrue(message, firstActual.date.contains("37")) // minutes of day

        assertNull(actual[1].number)
    }
}
