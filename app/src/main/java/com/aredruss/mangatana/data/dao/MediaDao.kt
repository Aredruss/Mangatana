package com.aredruss.mangatana.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aredruss.mangatana.data.database.MediaDb

@Dao
interface MediaDao {
    companion object {
        const val TABLE_NAME = "my_media"
    }

    // get
    @Query("SELECT * FROM $TABLE_NAME WHERE status =:status AND type = :type")
    suspend fun getEntriesByStatus(status: Int, type: String): List<MediaDb>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY id LIMIT(5)")
    suspend fun getRecentEntries(): List<MediaDb>

    // update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntry(entry: MediaDb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEntries(entries: List<MediaDb>)

    // delete
    @Delete
    suspend fun deleteEntry(mediaDb: MediaDb)

    @Delete
    suspend fun deleteEntries(entries: List<MediaDb>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearDatabase()

    // update
    @Query("UPDATE $TABLE_NAME SET status = :status WHERE mal_id = :malId AND type = :type")
    suspend fun updateStatus(malId: Long, status: Int, type: String)
}
