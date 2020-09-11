package io.github.fabiantauriello.moviecatalog.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import io.github.fabiantauriello.moviecatalog.app.MovieCatalog

/**
 * My Room database object
 *
 */
@Database(
    entities = [MediaItemEntity::class, WatchlistEntity::class],
    version = 6,
    exportSchema = false
)
abstract class MovieCatalogDatabase : RoomDatabase() {

    abstract val mediaItemDao: MediaItemDao
    abstract val watchlistDao: WatchlistDao

    companion object {

        // This will keep a reference to the database, once one has been created
        @Volatile
        private var INSTANCE: MovieCatalogDatabase? = null

        fun getInstance(): MovieCatalogDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        MovieCatalog.applicationContext(),
                        MovieCatalogDatabase::class.java,
                        "movie_catalog_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }

}

