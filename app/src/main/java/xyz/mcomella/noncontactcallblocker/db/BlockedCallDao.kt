package xyz.mcomella.noncontactcallblocker.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface BlockedCallDao {
    @Insert
    fun insertBlockedCalls(vararg blockedCalls: BlockedCallEntity)

    @Query("SELECT * from blocked_calls")
    fun loadBlockedCalls(): Array<BlockedCallEntity>
}