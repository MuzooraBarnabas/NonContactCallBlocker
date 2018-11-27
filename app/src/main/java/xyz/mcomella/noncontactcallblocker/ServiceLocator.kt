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

import xyz.mcomella.noncontactcallblocker.config.Config
import xyz.mcomella.noncontactcallblocker.db.AppDB
import xyz.mcomella.noncontactcallblocker.repository.BlockedCallRepository
import xyz.mcomella.noncontactcallblocker.repository.ContactsRepository

/**
 * Encapsulates all the dependencies of the app: see also the service locator pattern.
 */
class ServiceLocator(app: CallBlockApplication) {

    // We lazy load everything to minimize performance impact when the whole app is
    // not initialized (e.g. when blocking a call in the background).
    private val db by lazy { AppDB.create(app) }
    val blockedCallRepository by lazy { BlockedCallRepository(db.blockedCallDao()) }
    val contactsRepository by lazy { ContactsRepository(app.contentResolver) }

    val config by lazy { Config.create(app) }
}
