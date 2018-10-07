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

import android.net.Uri
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.TelecomManager
import kotlinx.coroutines.experimental.launch
import xyz.mcomella.noncontactcallblocker.config.Config
import xyz.mcomella.noncontactcallblocker.db.AppDB
import xyz.mcomella.noncontactcallblocker.db.AppDB.Companion.dbDispatcher
import xyz.mcomella.noncontactcallblocker.db.BlockedCallEntity
import java.util.*

/** The call blocking logic in the app. */
class CallBlockService : CallScreeningService() {

    override fun onScreenCall(callDetails: Call.Details) {
        val number = callDetails.intentExtras[TelecomManager.EXTRA_INCOMING_CALL_ADDRESS] as Uri? // tel:...
        val isCallBlocked = when {
            !Config.get().isBlockingEnabled -> false
            number == null -> true // It's an assumption this is an unknown number, but we want to block unknown numbers.
            else -> !Contacts.isNumberInContacts(contentResolver, number)
        }

        respondToCall(callDetails, getCallResponse(isCallBlocked))
        if (isCallBlocked) {
            val blockedCall = BlockedCallEntity(number?.schemeSpecificPart, Date(System.currentTimeMillis()))
            launch(dbDispatcher) {
                AppDB.db.blockedCallDao().insertBlockedCalls(blockedCall)
            }
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