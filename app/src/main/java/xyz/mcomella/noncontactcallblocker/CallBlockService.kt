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

package xyz.mcomella.noncontactcallblocker

import android.os.StrictMode
import android.telecom.Call
import android.telecom.CallScreeningService
import xyz.mcomella.noncontactcallblocker.ext.resetAfter
import xyz.mcomella.noncontactcallblocker.ext.serviceLocator
import java.util.Date

/** The call blocking logic in the app. */
class CallBlockService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val number = callDetails.handle

        // Strict mode override: this method needs to block and we need to read from disk to get contacts.
        val isCallBlocked = StrictMode.allowThreadDiskReads().resetAfter {
            when {
                !serviceLocator.config.isBlockingEnabled -> false
                number == null -> true // It's an assumption this is an unknown number, but we want to block unknown numbers.
                else -> !serviceLocator.contactsRepository.isNumberInContacts(number)

            }
        }

        respondToCall(callDetails, getCallResponse(isCallBlocked))
        if (isCallBlocked) {
            val numberStr = number?.schemeSpecificPart
            val date = Date(System.currentTimeMillis())
            serviceLocator.blockedCallRepository.onCallBlocked(numberStr, date)
        }
    }

    private fun getCallResponse(isCallBlocked: Boolean): CallResponse {
        val callResponseBuilder = CallResponse.Builder()
                .setDisallowCall(isCallBlocked)
        if (isCallBlocked) {
            callResponseBuilder
                    .setSkipCallLog(true)
                    .setSkipNotification(true)
        }
        return callResponseBuilder.build()
    }
}
