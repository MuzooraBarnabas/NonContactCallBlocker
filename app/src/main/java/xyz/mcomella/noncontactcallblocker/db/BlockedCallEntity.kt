package xyz.mcomella.noncontactcallblocker.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.util.Date

/** Table for blocked calls. */
@Entity(tableName = "blocked_calls")
data class BlockedCallEntity(
        val number: String?,
        val date: Date
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}
