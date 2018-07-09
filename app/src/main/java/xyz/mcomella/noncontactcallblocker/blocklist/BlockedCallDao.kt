package xyz.mcomella.noncontactcallblocker.blocklist

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

/** Accessor for block list. */
@Dao
interface BlockedCallDao {
    @Insert
    fun insertBlockedCalls(vararg blockedCalls: BlockedCallEntity)

    @Query("SELECT * FROM blocked_calls ORDER BY date DESC")
    fun loadBlockedCalls(): LiveData<List<BlockedCallEntity>>
}