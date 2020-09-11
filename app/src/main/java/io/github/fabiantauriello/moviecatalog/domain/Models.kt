package io.github.fabiantauriello.moviecatalog.domain

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MediaItem(
    @SerializedName("id")
    val mediaId: Int?,
    @SerializedName(value ="title", alternate = ["name"])
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    var isMovie: Boolean
) : Parcelable

data class MediaCategoryResponse(
    @SerializedName("results")
    val mediaItems: List<MediaItem>,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

data class MediaDetails(
    @SerializedName("id")
    val mediaId: Int?,
    @SerializedName(value ="title", alternate = ["name"])
    val title: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("overview")
    val overview: String?
)