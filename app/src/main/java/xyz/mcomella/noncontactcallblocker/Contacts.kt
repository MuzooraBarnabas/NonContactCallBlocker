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

import android.content.ContentResolver
import android.net.Uri
import android.provider.ContactsContract

object Contacts {

    fun isNumberInContacts(contentResolver: ContentResolver, number: Uri): Boolean {
        val queryUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, number.toString())
        return contentResolver.query(queryUri, emptyArray(), // Empty means
                null, null, null).use {
            it.count > 0
        }
    }
}