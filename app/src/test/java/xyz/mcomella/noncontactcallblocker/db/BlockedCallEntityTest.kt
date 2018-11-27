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

package xyz.mcomella.noncontactcallblocker.db

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.text.DateFormat
import java.text.DateFormat.SHORT
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale
import java.util.TimeZone

private val UTC = TimeZone.getTimeZone("UTC")

@RunWith(RobolectricTestRunner::class)
class BlockedCallEntityTest {

    @Test
    fun `WHEN converting to a UI call using UTC, US values THEN the output is formatted correctly`() {
        val calendar = GregorianCalendar(UTC).apply {
            clear()
            set(2018, 10, 22, 10, 5) // Month is 0-index
        }

        val entity = BlockedCallEntity("5555555555", calendar.time)
        val dateFormat = DateFormat.getDateTimeInstance(SHORT, SHORT, Locale.US).apply {
            timeZone = UTC
        }
        val actual = entity.toBlockedCall(dateFormat)

        assertEquals("(555) 555-5555", actual.number)
        assertEquals("11/22/18 10:05 AM", actual.date)
    }

    @Test
    fun `WHEN converting to a UI call using using the default values THEN the date is consistent with the platform's default TimeZone and locale`() {
        val expectedDate = GregorianCalendar(UTC).apply {
            clear()
            set(2018, 10, 22, 10, 5) // Month is 0-index
        }.time

        val actual = BlockedCallEntity(null, expectedDate).toBlockedCall()

        val dateFormat = DateFormat.getDateTimeInstance(SHORT, SHORT) // uses default timezone and locale.
        assertEquals(expectedDate, dateFormat.parse(actual.date))
    }

    @Test
    fun `WHEN converting to a UI call with a null number THEN a null number is returned`() {
        val actual = BlockedCallEntity(null, Date(1234)).toBlockedCall()
        assertNull(actual.number)
    }
}
