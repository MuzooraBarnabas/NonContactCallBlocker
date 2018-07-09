/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.noncontactcallblocker

import android.Manifest.permission.*
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.*
import android.support.annotation.CheckResult
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

private const val REQUEST_CODE_CONTACTS = 3636

typealias OnPermissionsGranted = () -> Unit

/**
 * Encapsulation of code related to permissions handing.
 *
 * Callers should:
 * - Call maybePromptForRequired *once* per Activity instance
 * - Call [onRequestPermissionsResult] in the calling Activity's method of the same name.
 * - Call [onActivityResult] in the calling Activity's method of the same name.
 */
object Permissions {

    data class PermissionsContext(
            internal val activity: Activity,
            internal val onSuccess: OnPermissionsGranted,
            internal var activeDeferred: CompletableDeferred<Unit>? = null
    )

    /**
     * Prompts users for permissions required by the app, if they have not already been granted.
     *
     * @return a [PermissionsContext] object that should be based into all Activity callbacks.
     */
    fun maybePromptForRequired(activity: Activity, uiCancelJob: Job, onSuccess: OnPermissionsGranted): PermissionsContext {
        val permissionsContext = PermissionsContext(activity, onSuccess)

        launch(UI + uiCancelJob, CoroutineStart.UNDISPATCHED) {
            maybePromptContacts(permissionsContext)
            onSuccess()
        }

        return permissionsContext
    }

    private fun maybePromptContacts(permissionsContext: PermissionsContext): Deferred<Unit> {
        val deferred = CompletableDeferred<Unit>()
        permissionsContext.activeDeferred = deferred

        val activity = permissionsContext.activity
        if (ContextCompat.checkSelfPermission(activity, READ_CONTACTS) == PERMISSION_GRANTED) {
            deferred.complete(Unit)
        } else {
            val message = getPromptContactsDialogMessage(activity, isDenied = false)
            getPromptContactsDialog(activity, message).show()
        }

        return deferred
    }

    /** Handles a permissions request result. */
    fun onRequestPermissionsResult(permissionsContext: PermissionsContext, requestCode: Int,
                                   permissions: Array<out String>, grantResults: IntArray) {
        // We only handle contacts here.
        if (requestCode != REQUEST_CODE_CONTACTS) {
            throw IllegalArgumentException("Unexpected permissions requested: $permissions")
        }

        if (grantResults[0] == PERMISSION_GRANTED) {
            permissionsContext.activeDeferred!!.complete(Unit) // If created in here, should never be null.
            permissionsContext.activeDeferred = null
        } else {
            val activity = permissionsContext.activity
            val message = getPromptContactsDialogMessage(permissionsContext.activity, isDenied = true)
            getPromptContactsDialog(activity, message).show()
        }
    }

    private fun getPromptContactsDialogMessage(context: Context, isDenied: Boolean): String {
        val appName = context.getString(R.string.app_name)
        val contactsNotShared = context.getString(R.string.dialog_permissions_contacts_not_shared)
        val msgId = if (isDenied) R.string.dialog_permissions_contacts_denied_message else R.string.dialog_permissions_contacts_message
        return context.getString(msgId, appName, contactsNotShared)
    }

    @CheckResult
    private fun getPromptContactsDialog(activity: Activity, message: String) = AlertDialog.Builder(activity)
            .setIcon(R.drawable.material_contacts)
            .setTitle(R.string.dialog_permissions_contacts_title)
            .setMessage(message)
            .setPositiveButton(R.string.dialog_permissions_okay) { _, _  ->
                requestContacts(activity)
            }
            .create()

    private fun requestContacts(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(READ_CONTACTS), REQUEST_CODE_CONTACTS)
    }
}
