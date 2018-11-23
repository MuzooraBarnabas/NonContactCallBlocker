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

import android.arch.core.executor.testing.InstantTaskExecutorRule
import android.arch.lifecycle.MutableLiveData
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import xyz.mcomella.noncontactcallblocker.repository.BlockedCallRepository
import java.util.*

class CallBlockListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var viewModel: CallBlockListViewModel
    private lateinit var blockedCalls: MutableLiveData<List<BlockedCallEntity>>

    @Before
    fun setUp() {
        blockedCalls = MutableLiveData()
        val repository = mock(BlockedCallRepository::class.java).also {
            `when`(it.getBlockedCalls()).thenReturn(blockedCalls)
        }
        viewModel = CallBlockListViewModel(repository)
    }

    @Test
    fun `WHEN an empty list is received THEN an empty list is returned`() {
        viewModel.blockedCalls.observeForever { assertEquals(emptyList<BlockedCallEntity>(), it) }
        blockedCalls.postValue(listOf())
    }

    @Test
    fun `WHEN a list of items is received THEN it is returned`() {
        val expected = listOf(
                BlockedCallEntity("5555555555", Date()),
                BlockedCallEntity("5556666666", Date(1234567))
        )
        viewModel.blockedCalls.observeForever { assertEquals(expected, it) }
        blockedCalls.postValue(expected.toList()) // copy to ensure reference equality isn't used.
    }
}
