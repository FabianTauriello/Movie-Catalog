package io.github.fabiantauriello.moviecatalog.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailCategoryItemBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.util.Utils

/**
 * adapter for recycler views in the category sections
 */
class MediaCategoryAdapter(
    private var mediaItems: List<MediaItem>?,
    private val listener: ThumbnailClickListener
) : RecyclerView.Adapter<MediaCategoryAdapter.ItemViewHolder>() {

    // creates view holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = DataBindingUtil.inflate<MediaThumbnailCategoryItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.media_thumbnail_category_item,
            parent,
            false
        )
        return ItemViewHolder(binding)
    }

    // binds data to view holder
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("LOG_TAG", "onBindViewHolder: ")

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

    class ItemViewHolder(
        val binding: MediaThumbnailCategoryItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

}