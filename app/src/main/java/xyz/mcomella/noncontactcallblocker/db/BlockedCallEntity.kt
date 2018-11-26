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

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.telephony.PhoneNumberUtils
import xyz.mcomella.noncontactcallblocker.ui.blocklist.BlockedCall
import java.text.DateFormat
import java.util.Date

private val defaultDateFormat = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)

/** Table for blocked calls. */
@Entity(tableName = "blocked_calls")
data class BlockedCallEntity(
        val number: String?,
        val date: Date
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0

    fun toBlockedCall(
            dateFormat: DateFormat = defaultDateFormat
    ): BlockedCall {
        val outNumber = number?.let { PhoneNumberUtils.formatNumber(it, "US") } // todo: country?
        val outDate = dateFormat.format(date)
        return BlockedCall(outNumber, outDate)
    }
}
