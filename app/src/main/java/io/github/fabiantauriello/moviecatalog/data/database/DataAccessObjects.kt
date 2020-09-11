package io.github.fabiantauriello.moviecatalog.data.database

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.fabiantauriello.moviecatalog.domain.MediaItem

/**
 * This is used for caching paginated data into a Room database.
 */
@Dao
interface MediaItemDao {
    // inserts a list of media items into the table
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(items: List<MediaItemEntity>)

    // deletes all of the media items
    @Query("DELETE FROM media_item")
    suspend fun clearAll()

    // takes a query string as a parameter and returns a PagingSource object for the list of media item results
    @Query("SELECT * FROM media_item WHERE isMovie == :isMovie")
    fun pagingSource(isMovie: Boolean): PagingSource<Int, MediaItemEntity>
}

/**
 * This is used to store a user's saved watchlist items into a Room database.
 */
@Dao
interface WatchlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: WatchlistEntity)

    @Query("SELECT * FROM watchlist")
    fun retrieveAll(): LiveData<List<MediaItem>>

    @Query("SELECT * FROM watchlist WHERE mediaId == :mediaId AND isMovie == :isMovie")
    fun retrieveWatchlistItem(mediaId: Int, isMovie: Boolean): WatchlistEntity

    @Query("DELETE FROM watchlist WHERE mediaId == :mediaId AND isMovie == :isMovie")
    fun delete(mediaId: Int?, isMovie: Boolean)
}