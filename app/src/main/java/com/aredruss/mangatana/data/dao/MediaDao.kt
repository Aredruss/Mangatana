package com.aredruss.mangatana.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.repo.JikanRepository

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

    @Query("SELECT * FROM $TABLE_NAME WHERE type=:type ORDER BY id DESC LIMIT 5")
    suspend fun getRecentEntries(type: String): List<MediaDb>

    @Query("SELECT COUNT(id) FROM $TABLE_NAME")
    suspend fun getEntryCount(): Long

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE status = ${MediaDb.FINISHED_STATUS}")
    suspend fun getFinishedCount(): Int

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE type = \"${JikanRepository.TYPE_ANIME}\"")
    suspend fun getAnimeCount(): Int

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE type = \"${JikanRepository.TYPE_MANGA}\"")
    suspend fun getMangaCount(): Int

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE type = \"${JikanRepository.TYPE_ANIME}\" and status = ${MediaDb.FINISHED_STATUS}")
    suspend fun getFinishedAnimeCount(): Int

    @Query("SELECT COUNT(id) FROM $TABLE_NAME WHERE type = \"${JikanRepository.TYPE_MANGA}\"  and status = ${MediaDb.FINISHED_STATUS}")
    suspend fun getFinishedMangaCount(): Int

    // update
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: MediaDb): Long

    // delete
    @Query("DELETE FROM $TABLE_NAME WHERE mal_id =:malId AND type = :type")
    suspend fun delete(malId: Long, type: String)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clearDatabase()

    // update
    @Query("UPDATE $TABLE_NAME SET status = :status AND is_starred =:isStarred WHERE mal_id = :malId AND type = :type")
    suspend fun updateEntry(malId: Long, type: String, status: Int, isStarred: Boolean)
}
