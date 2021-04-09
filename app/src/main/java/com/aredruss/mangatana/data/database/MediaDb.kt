package com.aredruss.mangatana.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.aredruss.mangatana.data.dao.MediaDao.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME, indices = [Index(value = ["mal_id", "type"], unique = true)])
data class MediaDb(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "mal_id")
    var malId: Long,
    @ColumnInfo(name = "status")
    var status: Int,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "type")
    var type: String,
    @ColumnInfo(name = "url")
    var url: String,
    @ColumnInfo(name = "image")
    var imageUrl: String,
    @ColumnInfo(name = "is_starred")
    var isStarred: Boolean
) {
    companion object {
        const val UNKNOWN_STATUS = -1
        const val ONGOING_STATUS = 1
        const val BACKLOG_STATUS = 2
        const val FINISHED_STATUS = 3
    }
}
