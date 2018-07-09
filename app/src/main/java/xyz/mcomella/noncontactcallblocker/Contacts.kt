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