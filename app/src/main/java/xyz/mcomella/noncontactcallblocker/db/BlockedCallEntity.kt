package xyz.mcomella.noncontactcallblocker.db

import android.arch.persistence.room.Entity
import java.util.Date

@Entity(tableName = "blocked_calls",
        primaryKeys = ["number", "date"])
data class BlockedCallEntity(
        val number: String?,
        val date: Date
)
