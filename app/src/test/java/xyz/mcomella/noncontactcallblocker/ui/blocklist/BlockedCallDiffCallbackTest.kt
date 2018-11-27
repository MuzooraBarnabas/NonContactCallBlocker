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

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class BlockedCallDiffCallbackTest {

    private lateinit var diffCallback: BlockedCallDiffCallback

    @Before
    fun setUp() {
        diffCallback = BlockedCallDiffCallback()
    }

    @Test
    fun `WHEN passed the same item THEN items are the same`() {
        val (call, _) = generateTwoDifferentItems()
        assertTrue(diffCallback.areItemsTheSame(call, call))
    }

    @Test
    fun `WHEN passed the same item THEN contents are the same`() {
        val (call, _) = generateTwoDifferentItems()
        assertTrue(diffCallback.areContentsTheSame(call, call))
    }

    @Test
    fun `WHEN passed a different item with the same values THEN items are the same`() {
        val (call1, _) = generateTwoDifferentItems()
        val (call2, _) = generateTwoDifferentItems()
        assertTrue(diffCallback.areItemsTheSame(call1, call2))
    }

    @Test
    fun `WHEN passed a different item with the same values THEN contents are the same`() {
        val (call1, _) = generateTwoDifferentItems()
        val (call2, _) = generateTwoDifferentItems()
        assertTrue(diffCallback.areContentsTheSame(call1, call2))
    }

    @Test
    fun `WHEN passed a different item with different values THEN items are not the same`() {
        val (call1, call2) = generateTwoDifferentItems()
        assertFalse(diffCallback.areItemsTheSame(call1, call2))
    }

    @Test
    fun `WHEN passed a different item with different values THEN contents are not the same`() {
        val (call1, call2) = generateTwoDifferentItems()
        assertFalse(diffCallback.areItemsTheSame(call1, call2))
    }

    private fun generateTwoDifferentItems(): Pair<BlockedCall, BlockedCall> = Pair(
            BlockedCall("555-555-5555", "11/22/04 10:05 AM"),
            BlockedCall("555-444-4444", "1/2/04 1:05 AM")
    )
}
