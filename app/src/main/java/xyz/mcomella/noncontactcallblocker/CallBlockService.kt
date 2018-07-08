/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.noncontactcallblocker

import android.net.Uri
import android.telecom.Call
import android.telecom.CallScreeningService
import android.telecom.TelecomManager
import xyz.mcomella.noncontactcallblocker.config.Config
import xyz.mcomella.noncontactcallblocker.db.AppDB
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
            val blockedCall = BlockedCallEntity(number?.host, Date(System.currentTimeMillis()))
            AppDB.db.blockedCallDao().insertBlockedCalls(blockedCall)
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