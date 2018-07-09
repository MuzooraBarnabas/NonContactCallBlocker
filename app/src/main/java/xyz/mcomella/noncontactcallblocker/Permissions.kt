/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.noncontactcallblocker

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager.*
import android.support.annotation.CheckResult
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog

private const val REQUEST_CODE_CONTACTS = 3636

/**
 * Encapsulation of code related to permissions handing.
 *
 * Callers should call [onRequestPermissionsResult] in the Activity's same named method.
 */
object Permissions {

    /**
     * Prompts users for the contacts permission, if it has not already been granted.
     *
     * @return true if permission needed to be requested, false otherwise.
     */
    fun maybePromptContacts(activity: Activity): Boolean {
        if (ContextCompat.checkSelfPermission(activity, READ_CONTACTS) == PERMISSION_GRANTED) {
            return false
        }

        showPromptContactsDialog(activity, getPromptContactsDialogMessage(activity, isDenied = false))

        return true
    }

    /**
     * Handles a permissions request result.
     *
     * @return true if the permission was granted, false otherwise.
     */
    @CheckResult
    fun onRequestPermissionsResult(activity: Activity, requestCode: Int, permissions: Array<out String>,
                                   grantResults: IntArray): Boolean {
        if (requestCode != REQUEST_CODE_CONTACTS) {
            throw IllegalArgumentException("Unexpected permissions requested: $permissions")
        }

        if (grantResults[0] == PERMISSION_GRANTED) { return true }
        showPromptContactsDialog(activity, getPromptContactsDialogMessage(activity, isDenied = true))

        return false
    }

    private fun getPromptContactsDialogMessage(context: Context, isDenied: Boolean): String {
        val appName = context.getString(R.string.app_name)
        val contactsNotShared = context.getString(R.string.dialog_permissions_contacts_not_shared)
        val msgId = if (isDenied) R.string.dialog_permissions_contacts_denied_message else R.string.dialog_permissions_contacts_message
        return context.getString(msgId, appName, contactsNotShared)
    }

    private fun showPromptContactsDialog(activity: Activity, message: String) {
        AlertDialog.Builder(activity)
                .setIcon(R.drawable.material_contacts)
                .setTitle(R.string.dialog_permissions_contacts_title)
                .setMessage(message)
                .setPositiveButton(R.string.dialog_permissions_okay) { _, _  ->
                    requestContacts(activity)
                }
                .create()
                .show()
    }

    private fun requestContacts(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(READ_CONTACTS), REQUEST_CODE_CONTACTS)
    }
}
