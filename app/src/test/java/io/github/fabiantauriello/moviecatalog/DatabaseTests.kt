package io.github.fabiantauriello.moviecatalog

import androidx.room.Room
import io.github.fabiantauriello.moviecatalog.data.database.MovieCatalogDatabase
import io.github.fabiantauriello.moviecatalog.data.database.WatchlistDao
import io.github.fabiantauriello.moviecatalog.data.database.WatchlistEntity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.runner.RunWith
import org.junit.Assert.*
import java.io.IOException

class DatabaseTests {

    private lateinit var watchlistItems: List<WatchlistEntity>
    private lateinit var watchlistDao: WatchlistDao
    private lateinit var db: MovieCatalogDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, MovieCatalogDatabase::class.java)
            // Allowing main thread queries, just for testing.
            .allowMainThreadQueries()
            .build()
        watchlistDao = db.watchlistDao
    }

    @Before
    fun setupDataObjects() {
        watchlistItems = listOf(
            WatchlistEntity(
                1,
                "Titanic",
                "/kqjL17yufvn9OVLyXYpvtyrFfak.jpg",
                "Big ship sinks",
                true
            ),
            WatchlistEntity(
                2,
                "Friends",
                "/kqjL17yufvq9OVLyx49ftyrssak.jpg",
                "bunch of friends hang out at a coffee shop",
                false
            )
        )
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    // WatchlistDao tests

    fun testWatchlistDao() {
        watchlistDao.insert(watchlistItems[0])
        watchlistDao.insert(watchlistItems[1])

        val titanic = watchlistDao.retrieveWatchlistItem(1, true)
        val friends = watchlistDao.retrieveWatchlistItem(2, false)

        assertEquals(titanic, watchlistItems[0])
        assertEquals(friends, watchlistItems[1])
    }

}