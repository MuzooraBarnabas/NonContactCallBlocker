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

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import kotlinx.coroutines.experimental.asCoroutineDispatcher
import java.util.concurrent.Executors

private const val DB_NAME = "noncontactcallblocker"

/** Application database. */
@Database(entities = [BlockedCallEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun blockedCallDao(): BlockedCallDao

    companion object {
        val dbDispatcher = Executors.newFixedThreadPool(2).asCoroutineDispatcher()

        fun create(context: Context): AppDB {
            return Room.databaseBuilder(context.applicationContext, AppDB::class.java, DB_NAME).build()
        }
    }
}
