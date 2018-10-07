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

package xyz.mcomella.noncontactcallblocker.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import xyz.mcomella.noncontactcallblocker.blocklist.BlockedCallDao
import xyz.mcomella.noncontactcallblocker.blocklist.BlockedCallEntity
import java.util.concurrent.Executors

private const val DB_NAME = "noncontactcallblocker"

/** Application database. */
@Database(entities = [BlockedCallEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun blockedCallDao(): BlockedCallDao

    companion object {
        private lateinit var _db: AppDB
        val db get() = _db

        val dbDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

        fun init(context: Context) {
            _db = Room.databaseBuilder(context.applicationContext, AppDB::class.java, DB_NAME).build()
        }
    }
}
