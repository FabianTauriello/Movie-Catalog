package io.github.fabiantauriello.moviecatalog.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailGridItemBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.util.Utils

/**
 * page adapter for the recycler views in the see all sections
 */
class MediaGridAdapterPaged(
    private val listener: ThumbnailClickListener
) : PagingDataAdapter<MediaItem, MediaGridAdapterPaged.PagingViewHolder>(THUMBNAIL_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingViewHolder {
        val binding = DataBindingUtil.inflate<MediaThumbnailGridItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.media_thumbnail_grid_item,
            parent,
            false
        )
        return PagingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PagingViewHolder, position: Int) {
        val thumbnail = getItem(position)
        if (thumbnail != null) {
            // set title
            holder.binding.textTitle.text = thumbnail.title
            // set poster image
            if(thumbnail.posterPath != null) {
                Utils.setThumbnailImage(
                    thumbnail.posterPath,
                    holder.binding.imagePoster
                )
            }
        }
        // set click listener
        holder.binding.card.setOnClickListener {
            // send the callback to the see all fragment
            listener.onThumbnailClick(getItem(position)!!)
        }
    }

    class PagingViewHolder(
        val binding: MediaThumbnailGridItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    companion object {
        private val THUMBNAIL_COMPARATOR = object : DiffUtil.ItemCallback<MediaItem>() {
            override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
                oldItem.title == newItem.title

            override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem): Boolean =
                oldItem == newItem
        }
    }

}