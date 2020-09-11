package io.github.fabiantauriello.moviecatalog.ui

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.util.Utils

/**
 * extension methods that sit between a view and bound
 * data to provide custom behavior when the data changes (Binding Adapter)
 * In this case, the custom behavior is to call Glide to load an image
 * from a URL into an ImageView.
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if (imgUrl != null)  {
        Utils.setDetailsBannerImage(imgUrl, imgView)
    } else {
        imgView.setImageResource(R.drawable.ic_broken_image)
    }
}