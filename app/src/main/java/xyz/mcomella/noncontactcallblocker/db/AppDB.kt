package xyz.mcomella.noncontactcallblocker.db

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.TypeConverters
import android.content.Context

private const val DB_NAME = "noncontactcallblocker"

@Database(entities = [BlockedCallEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDB : RoomDatabase() {
    abstract fun blockedCallDao(): BlockedCallDao

    companion object {
        private lateinit var _db: AppDB
        val db get() = _db

        fun init(context: Context) {
            _db = Room.databaseBuilder(context.applicationContext, AppDB::class.java, DB_NAME).build()
        }
    }
}
