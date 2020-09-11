package io.github.fabiantauriello.moviecatalog.repository

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.app.MovieCatalog
import io.github.fabiantauriello.moviecatalog.data.RemoteMediator
import io.github.fabiantauriello.moviecatalog.data.database.MediaItemEntity
import io.github.fabiantauriello.moviecatalog.data.database.MovieCatalogDatabase
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseApi
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseService
import io.github.fabiantauriello.moviecatalog.domain.MediaDetails
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

/**
 *  Isolates data sources from the rest of the app.
 *  A repository mediates between data sources and the rest of the app.
 */
class Repository {

    // Movie data
    var moviesLatest: MutableLiveData<MediaItem> = MutableLiveData()
    var moviesNowPlaying: MutableLiveData<List<MediaItem>> = MutableLiveData()
    var moviesPopular: MutableLiveData<List<MediaItem>> = MutableLiveData()
    var moviesTopRated: MutableLiveData<List<MediaItem>> = MutableLiveData()
    var moviesUpcoming: MutableLiveData<List<MediaItem>> = MutableLiveData()

    // TV data
    var tvLatest: MutableLiveData<MediaItem> = MutableLiveData()
    var tvAiringToday: MutableLiveData<List<MediaItem>> = MutableLiveData()
    var tvOnTheAir: MutableLiveData<List<MediaItem>> = MutableLiveData()
    var tvPopular: MutableLiveData<List<MediaItem>> = MutableLiveData()
    var tvTopRated: MutableLiveData<List<MediaItem>> = MutableLiveData()

    // Media Details
    var mediaDetails: MutableLiveData<MediaDetails> = MutableLiveData()

    var isInWatchlist: MutableLiveData<Boolean> = MutableLiveData(false)

    // capture the items found from the user's search. NOTE: only the first page (20 items) of search results is retrieved from the API
    var searchResults: MutableLiveData<List<MediaItem>> = MutableLiveData()

    val watchList: LiveData<List<MediaItem>> =
        MovieCatalogDatabase.getInstance().watchlistDao.retrieveAll()

    suspend fun fetchMovieData() {
        // get latest movies
        val response = TheMovieDatabaseApi.retrofitService.getMovieLatest()
        if (response.isSuccessful) {
            response.body()?.isMovie = true
            moviesLatest.postValue(response.body())
        }
        // get the rest of the data
        fetchMovieDataForCategory(TheMovieDatabaseService.MOVIE_NOW_PLAYING)
        fetchMovieDataForCategory(TheMovieDatabaseService.MOVIE_POPULAR)
        fetchMovieDataForCategory(TheMovieDatabaseService.MOVIE_TOP_RATED)
        fetchMovieDataForCategory(TheMovieDatabaseService.MOVIE_UPCOMING)
    }

    /**
     * retrieves movie data for display in the "Movie Categories (home)" screen by updating movie live data
     */
    private suspend fun fetchMovieDataForCategory(category: String) {
        try {
            val response = TheMovieDatabaseApi.retrofitService.getMediaListDataForCategory(category)
            val movieData = response.mediaItems
            for (item in movieData) {
                item.isMovie = true
            }
            when (category) {
                TheMovieDatabaseService.MOVIE_NOW_PLAYING -> moviesNowPlaying.postValue(movieData)
                TheMovieDatabaseService.MOVIE_POPULAR -> moviesPopular.postValue(movieData)
                TheMovieDatabaseService.MOVIE_TOP_RATED -> moviesTopRated.postValue(movieData)
                TheMovieDatabaseService.MOVIE_UPCOMING -> moviesUpcoming.postValue(movieData)
            }
        } catch (exception: IOException) {
            Toast.makeText(
                MovieCatalog.applicationContext(),
                MovieCatalog.applicationContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        } catch (exception: HttpException) {
            Toast.makeText(
                MovieCatalog.applicationContext(),
                MovieCatalog.applicationContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    suspend fun fetchTvData() {
        // get latest tv
        val response = TheMovieDatabaseApi.retrofitService.getTelevisionLatest()
        if (response.isSuccessful) {
            tvLatest.postValue(response.body())
        }
        // get the rest of the data
        fetchTvDataForCategory(TheMovieDatabaseService.TV_AIRING_TODAY)
        fetchTvDataForCategory(TheMovieDatabaseService.TV_ON_THE_AIR)
        fetchTvDataForCategory(TheMovieDatabaseService.TV_POPULAR)
        fetchTvDataForCategory(TheMovieDatabaseService.TV_TOP_RATED)
    }

    /**
     * retrieves tv data for display in the "TV Categories" screen by updating tv live data
     */
    private suspend fun fetchTvDataForCategory(category: String) {
        try {
            val response = TheMovieDatabaseApi.retrofitService.getMediaListDataForCategory(category)
            val tvData = response.mediaItems
            when (category) {
                TheMovieDatabaseService.TV_AIRING_TODAY -> tvAiringToday.postValue(tvData)
                TheMovieDatabaseService.TV_ON_THE_AIR -> tvOnTheAir.postValue(tvData)
                TheMovieDatabaseService.TV_POPULAR -> tvPopular.postValue(tvData)
                TheMovieDatabaseService.TV_TOP_RATED -> tvTopRated.postValue(tvData)
            }
        } catch (exception: IOException) {
            Toast.makeText(
                MovieCatalog.applicationContext(),
                MovieCatalog.applicationContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        } catch (exception: HttpException) {
            Toast.makeText(
                MovieCatalog.applicationContext(),
                MovieCatalog.applicationContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * create stream of data for showing MANY media items.
     */
    fun createPagerFlow(
        endpoint: String,
        remoteMediator: RemoteMediator
    ): Flow<PagingData<MediaItemEntity>> {

        val isMovie = endpoint.substringBefore("/") == "movie"

        // retrieve paging source (local Room db)
        val pagingSourceFactory =
            { MovieCatalogDatabase.getInstance().mediaItemDao.pagingSource(isMovie) }

        val pager = Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = true),
            remoteMediator = remoteMediator,
            pagingSourceFactory = pagingSourceFactory
        )

        return pager.flow
    }

    /**
     * update isInWatchlist if the item clicked on is already in the watchlist.
     */
    fun checkIfMediaIsInWatchlist(mediaId: Int?, isMovie: Boolean) {
        if (mediaId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                val item = MovieCatalogDatabase.getInstance().watchlistDao.retrieveWatchlistItem(
                    mediaId,
                    isMovie
                )
                isInWatchlist.postValue(item != null)
            }
        }
    }

    /**
     * search function for finding a media item based on the user's query
     */
    suspend fun searchApi(endpoint: String, searchQuery: String) {
        try {
            val response =
                TheMovieDatabaseApi.retrofitService.getSearchResults(endpoint, searchQuery)
            val isMovie = endpoint.substringAfter("/") == "movie"
            response.mediaItems.map { it.isMovie = isMovie }
            searchResults.postValue(response.mediaItems)
        } catch (exception: IOException) {
            Toast.makeText(
                MovieCatalog.applicationContext(),
                MovieCatalog.applicationContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        } catch (exception: HttpException) {
            Toast.makeText(
                MovieCatalog.applicationContext(),
                MovieCatalog.applicationContext().getString(R.string.error_message),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * get more details about a media item (including an overview)
     */
    suspend fun fetchMediaDetails(mediaId: Int?, isMovie: Boolean) {
        // media id might (in rare circumstances) be null so check first because details cannot be retrieved otherwise
        if (mediaId != null) {
            try {
                val response = if (isMovie) {
                    TheMovieDatabaseApi.retrofitService.getMovieDetails(mediaId)
                } else {
                    TheMovieDatabaseApi.retrofitService.getTvDetails(mediaId)
                }
                mediaDetails.value = response
            } catch (exception: IOException) {
                Toast.makeText(
                    MovieCatalog.applicationContext(),
                    MovieCatalog.applicationContext().getString(R.string.error_message),
                    Toast.LENGTH_SHORT
                ).show()
            } catch (exception: HttpException) {
                Toast.makeText(
                    MovieCatalog.applicationContext(),
                    MovieCatalog.applicationContext().getString(R.string.error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


}