package io.github.fabiantauriello.moviecatalog.data.network

import io.github.fabiantauriello.moviecatalog.domain.MediaCategoryResponse
import io.github.fabiantauriello.moviecatalog.domain.MediaDetails
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

// My api key
const val API_KEY = "bedf4bf97062b6773a4994a736a78b51"

/**
 * A retrofit service to fetch Movie data.
 */
interface TheMovieDatabaseService {

    @GET("movie/latest")
    suspend fun getMovieLatest(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MediaItem>

    @GET("tv/latest")
    suspend fun getTelevisionLatest(
        @Query("api_key") apiKey: String = API_KEY
    ): Response<MediaItem>

    @GET("{endpoint}")
    suspend fun getMediaListDataForCategory(
        @Path("endpoint", encoded = true) endpoint: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): MediaCategoryResponse

    @GET("{endpoint}")
    suspend fun getSearchResults(
        @Path("endpoint", encoded = true) endpoint: String,
        @Query("query") searchQuery: String,
        @Query("page") page: Int = 1,
        @Query("api_key") apiKey: String = API_KEY
    ): MediaCategoryResponse

    @GET("movie/{id}")
    suspend fun getMovieDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MediaDetails

    @GET("tv/{id}")
    suspend fun getTvDetails(
        @Path("id") id: Int,
        @Query("api_key") apiKey: String = API_KEY
    ): MediaDetails

    // endpoint constants
    companion object Endpoints {
        const val MOVIE_NOW_PLAYING = "movie/now_playing"
        const val MOVIE_POPULAR = "movie/popular"
        const val MOVIE_TOP_RATED = "movie/top_rated"
        const val MOVIE_UPCOMING = "movie/upcoming"
        const val TV_AIRING_TODAY = "tv/airing_today"
        const val TV_ON_THE_AIR = "tv/on_the_air"
        const val TV_POPULAR = "tv/popular"
        const val TV_TOP_RATED = "tv/top_rated"
        const val SEARCH = "search/"
    }

}

/**
 * Main entry point for network access.
 * This is a singleton
 */
object TheMovieDatabaseApi {

    private const val BASE_URL = "https://api.themoviedb.org/3/"

    // Configure retrofit to parse JSON
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService: TheMovieDatabaseService =
        retrofit.create(TheMovieDatabaseService::class.java)

}