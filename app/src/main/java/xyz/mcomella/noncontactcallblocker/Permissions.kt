/* This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/. */

package xyz.mcomella.noncontactcallblocker

import android.Manifest.permission.READ_CONTACTS
import android.app.Activity
import android.app.role.RoleManager
import android.content.Context
import androidx.annotation.CheckResult
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker.PERMISSION_GRANTED
import androidx.core.content.getSystemService
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.CoroutineStart
import kotlinx.coroutines.experimental.Deferred
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import xyz.mcomella.noncontactcallblocker.Permissions.onActivityResult
import xyz.mcomella.noncontactcallblocker.Permissions.onRequestPermissionsResult

private const val REQUEST_CODE_CONTACTS = 3636
private const val REQUEST_CODE_CALL_SCREENING = REQUEST_CODE_CONTACTS + 1

typealias OnPermissionsGranted = () -> Unit

/**
 * Encapsulation of code related to permissions handing.
 *
 * Callers should:
 * - Call maybePromptForRequired (e.g. onStart)
 * - Call [onRequestPermissionsResult] in the calling Activity's method of the same name.
 * - Call [onActivityResult] in the calling Activity's method of the same name.
 */
object Permissions {

    data class PermissionsContext(
        internal val activity: Activity,
        internal var activeDeferred: CompletableDeferred<Unit>? = null,
        internal var activeDialog: AlertDialog? = null
    )

    /**
     * Prompts users for permissions required by the app, if they have not already been granted.
     * This method will keep focus on the user using dialogs until permissions are granted. It will
     * not take into account if permissions are revoked while this method has focus.
     *
     * @return a [PermissionsContext] object that should be passed into all Activity callbacks.
     */
    fun maybePromptForRequired(
        existingPermissionsContext: PermissionsContext?,
        activity: Activity,
        uiCancelJob: Job,
        onSuccess: OnPermissionsGranted
    ): PermissionsContext {
        if (existingPermissionsContext?.activeDeferred != null) { // Already active request.
            return existingPermissionsContext
        }

        val permissionsContext = PermissionsContext(activity)
        launch(UI + uiCancelJob, CoroutineStart.UNDISPATCHED) {
            maybePromptContacts(permissionsContext).await()
            maybePromptCallScreeningService(permissionsContext).await()
            onSuccess()
        }

        return permissionsContext
    }

    private fun maybePromptContacts(permissionsContext: PermissionsContext): Deferred<Unit> {
        val deferred = CompletableDeferred<Unit>()
        permissionsContext.activeDeferred = deferred

        val activity = permissionsContext.activity
        if (ContextCompat.checkSelfPermission(activity, READ_CONTACTS) == PERMISSION_GRANTED) {
            complete(permissionsContext)
        } else {
            val message = getPromptContactsDialogMessage(activity, isDenied = false)
            getPromptContactsDialog(activity, message).show()
        }

        return deferred
    }

    private fun maybePromptCallScreeningService(permissionsContext: PermissionsContext): Deferred<Unit> {
        val deferred = CompletableDeferred<Unit>()
        permissionsContext.activeDeferred = deferred

        requestCallScreen(permissionsContext.activity)

        return deferred
    }

    /** Handles a permissions request result. */
    fun onRequestPermissionsResult(
        maybePermissionsContext: PermissionsContext?,
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode != REQUEST_CODE_CONTACTS) {
            throw IllegalArgumentException("Unexpected permissions requested: $permissions")
        }

        val permissionsContext = maybePermissionsContext!! // Should be non-null, nullable for easier caller.
        if (grantResults[0] == PERMISSION_GRANTED) {
            complete(permissionsContext)
        } else {
            val activity = permissionsContext.activity
            val message = getPromptContactsDialogMessage(permissionsContext.activity, isDenied = true)
            getPromptContactsDialog(activity, message).show()
        }
    }

    /** Handle an activity result for permissions. */
    fun onActivityResult(maybePermissionsContext: PermissionsContext?, requestCode: Int, resultCode: Int) {
        if (requestCode != REQUEST_CODE_CALL_SCREENING) { return }

        val permissionsContext = maybePermissionsContext!! // Should be non-null for our requests.
        when (resultCode) {
            Activity.RESULT_OK -> complete(permissionsContext)

            Activity.RESULT_CANCELED -> {
                val activity = permissionsContext.activity
                val message = getPromptCallScreenDialogMessage(activity)
                getPromptCallScreenDialog(activity, message).show()
            }

            else -> throw IllegalArgumentException("Unknown result code: $resultCode")
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
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_permissions_okay) { _, _ ->
                requestContacts(activity)
            }
            .create()

    private fun requestContacts(activity: Activity) {
        ActivityCompat.requestPermissions(activity, arrayOf(READ_CONTACTS), REQUEST_CODE_CONTACTS)
    }

    private fun getPromptCallScreenDialogMessage(context: Context): String {
        val appName = context.getString(R.string.app_name)
        return context.getString(R.string.dialog_call_screen_denied_message, appName)
    }

    @CheckResult
    private fun getPromptCallScreenDialog(activity: Activity, message: String) = AlertDialog.Builder(activity)
            .setIcon(R.drawable.material_phone)
            .setTitle(R.string.dialog_call_screen_title)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(R.string.dialog_permissions_okay) { _, _ ->
                requestCallScreen(activity)
            }
            .create()

    private fun requestCallScreen(activity: Activity) {
        val roleManager = activity.getSystemService<RoleManager>()!!
        val intent = roleManager.createRequestRoleIntent("android.app.role.CALL_SCREENING")
        activity.startActivityForResult(intent, REQUEST_CODE_CALL_SCREENING)
    }

    private fun complete(permissionsContext: PermissionsContext) = with(permissionsContext) {
        activeDeferred!!.complete(Unit)
        activeDeferred = null
        activeDialog = null
    }
}
