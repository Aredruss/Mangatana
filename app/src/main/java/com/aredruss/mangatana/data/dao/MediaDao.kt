package com.aredruss.mangatana.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.aredruss.mangatana.data.database.MediaDb

@Dao
interface MediaDao {
    companion object {
        const val TABLE_NAME = "my_media"
    }

    // get
    @Query("SELECT * FROM $TABLE_NAME WHERE status =:status AND type = :type")
    suspend fun getEntriesByStatus(status: Int, type: String): List<MediaDb>

    @Query("SELECT * FROM $TABLE_NAME WHERE mal_id =:malId AND type = :type")
    suspend fun getEntryByIdType(malId: Long, type: String): MediaDb?

    @Query("SELECT * FROM $TABLE_NAME WHERE type=:type AND is_starred = 1")
    suspend fun getStarredEntries(type: String): List<MediaDb>

    @Query("SELECT * FROM $TABLE_NAME WHERE type = :type AND status = :status AND title LIKE :query")
    suspend fun getEntriesByQuery(type: String, status: Int, query: String): List<MediaDb>

    @Query("SELECT * FROM $TABLE_NAME WHERE type = :type AND is_starred = 1 AND title LIKE :query")
    suspend fun getEntriesByQueryStatus(type: String, query: String): List<MediaDb>

    // update
    @Transaction
    suspend fun upsertEntry(entry: MediaDb) {
        if (insertEntry(entry) == -1L) {
            updateEntry(entry)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEntry(entry: MediaDb): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateEntry(entry: MediaDb)

    // delete
    @Query("DELETE FROM $TABLE_NAME WHERE mal_id =:malId AND type = :type")
    suspend fun delete(malId: Long, type: String)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearDatabase()

    // update
    @Query("UPDATE $TABLE_NAME SET status = :status AND is_starred =:isStarred WHERE mal_id = :malId AND type = :type")
    suspend fun updateEntry(malId: Long, type: String, status: Int, isStarred: Boolean)
}
