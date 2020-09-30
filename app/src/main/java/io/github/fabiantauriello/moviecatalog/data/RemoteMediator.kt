package io.github.fabiantauriello.moviecatalog.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import io.github.fabiantauriello.moviecatalog.data.database.MediaItemEntity
import io.github.fabiantauriello.moviecatalog.data.database.MovieCatalogDatabase
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseApi
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseService
import io.github.fabiantauriello.moviecatalog.util.Utils.asEntityModels
import retrofit2.HttpException
import java.io.IOException

private var THE_MOVIE_DATABASE_PAGE_INDEX = 1

/**
 * Helps implement offline caching and pagination from network and database. RemoteMediator is part of the Paging 3.0 library
 * The general idea is that a local Room database will be the source of truth for displaying "See All" data.
 * Data will always be loaded from there. However, whenever we don't have any more data in the database, we
 * request more from the API and then save it in the database.
 */
@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(
    private val endpoint: String,
    private val database: MovieCatalogDatabase = MovieCatalogDatabase.getInstance(), // the Room database that serves as a local cache
    private val service: TheMovieDatabaseService = TheMovieDatabaseApi.retrofitService // an API instance for the backend service
) : RemoteMediator<Int, MediaItemEntity>() {

    // This method will be called whenever we need to load more data from the network.
    // It returns a MediatorResult object, that can either be:
    // Error - if we got an error while requesting data from the network
    // Success - If we successfully got data from the network. Here, we also need to pass in a signal that tells whether more data can be loaded or not
    override suspend fun load(
        loadType: LoadType,
        pagingState: PagingState<Int, MediaItemEntity>
    ): MediatorResult {
        return try {
            when (loadType) {
                // initial load
                LoadType.REFRESH -> THE_MOVIE_DATABASE_PAGE_INDEX = 1
                // load data for previous page (user is scrolling up)
                LoadType.PREPEND -> return MediatorResult.Success(true)
                // load data for next page (user is scrolling down)
                LoadType.APPEND -> THE_MOVIE_DATABASE_PAGE_INDEX++
            }

            // call api
            val response =
                service.getMediaListDataForCategory(endpoint, THE_MOVIE_DATABASE_PAGE_INDEX)

            database.withTransaction {
                // If this a new query, then we clear the database.
                if (loadType == LoadType.REFRESH) {
                    database.mediaItemDao.clearAll()
                }

                val isMovie = endpoint.substringBefore("/") == "movie"

                // Insert new media into cache, which invalidates the
                // current PagingData, allowing Paging to present the updates
                // in the DB.
                database.mediaItemDao.insertAll(response.mediaItems.asEntityModels(isMovie))
            }
            // if the page index matches the totalPages property in the api response, there is no more data to request
            MediatorResult.Success(THE_MOVIE_DATABASE_PAGE_INDEX == response.totalPages)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}