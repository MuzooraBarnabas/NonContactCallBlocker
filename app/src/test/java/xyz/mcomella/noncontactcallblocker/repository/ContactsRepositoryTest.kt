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

package xyz.mcomella.noncontactcallblocker.repository

import android.content.ContentResolver
import android.net.Uri
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.robolectric.RobolectricTestRunner
import android.provider.ContactsContract.CommonDataKinds.Phone
import android.provider.ContactsContract.Contacts.Entity.RAW_CONTACT_ID
import android.content.ContentValues
import android.database.DatabaseUtils
import android.provider.ContactsContract
import org.junit.Assert.assertTrue


@RunWith(RobolectricTestRunner::class)
class ContactsRepositoryTest {

    private lateinit var repo: ContactsRepository
    private lateinit var contentResolver: ContentResolver

    @Before
    fun setUp() {
        contentResolver = spy(ContentResolver::class.java)
        repo = ContactsRepository(contentResolver)
    }

    @Test
    fun `WHEN passed invalid URI THEN number is not in contacts`() {
        assertFalse(repo.isNumberInContacts(Uri.parse("invalid uri")))
        // tel:6505555555
    }

    // todo: test ContactsRepository. integration test?
    @Test
    fun insertTest() {
        val values = ContentValues()
        values.put(ContactsContract.Data.RAW_CONTACT_ID, 1)
        values.put(ContactsContract.Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
        values.put(Phone.NUMBER, "1-800-GOOG-411")
        values.put(Phone.TYPE, Phone.TYPE_CUSTOM)
        values.put(Phone.LABEL, "free directory assistance")
        val dataUri = contentResolver.insert(ContactsContract.Data.CONTENT_URI, values)
        println(dataUri)

//        val contentUri: Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_FILTER_URI, "")

        val cursor = contentResolver.query(dataUri, null, null, null, null)
        DatabaseUtils.dumpCursor(cursor)

        assertTrue(repo.isNumberInContacts(Uri.parse("tel:1800GOOG411")))
    }
}
