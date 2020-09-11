package io.github.fabiantauriello.moviecatalog.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailGridItemBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.util.Utils

/**
 * adapter for the watchlist recycler view and the search results
 */
class MediaGridAdapter(
    private var mediaItems: List<MediaItem>?,
    private val listener: ThumbnailClickListener
) : RecyclerView.Adapter<MediaGridAdapter.ItemViewHolder>() {

    // creates view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<MediaThumbnailGridItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.media_thumbnail_grid_item,
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    // binds data to view holder
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {

        val mediaItem = mediaItems?.get(position)

        // set title
        holder.binding.textTitle.text = mediaItem?.title
        // set poster image
        Utils.setThumbnailImage(
            mediaItems?.get(position)?.posterPath,
            holder.binding.imagePoster
        )
        // set click listener
        holder.binding.card.setOnClickListener {
            // send the callback to the movie/tv fragment
            listener.onThumbnailClick(mediaItem!!)
        }

    }

    override fun getItemCount() = mediaItems?.size ?: 0

    fun updateData(newMedia: List<MediaItem>) {
        mediaItems = newMedia
        notifyDataSetChanged()
    }

    fun filterList(query: String) {
        mediaItems = mediaItems?.filter {
            if (it.title != null) {
                it.title.contains(query, true)
            } else {
                false
            }
        }
        notifyDataSetChanged()
    }

    class ItemViewHolder(
        val binding: MediaThumbnailGridItemBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

