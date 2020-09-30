package io.github.fabiantauriello.moviecatalog.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.data.database.MovieCatalogDatabase
import io.github.fabiantauriello.moviecatalog.databinding.FragmentDetailsBinding
import io.github.fabiantauriello.moviecatalog.util.Utils.asWatchlistEntity
import io.github.fabiantauriello.moviecatalog.viewmodels.DetailsViewModel
import io.github.fabiantauriello.moviecatalog.viewmodels.DetailsViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Displays a small amount of details about a specific media item.
 */
class DetailsFragment : Fragment() {

    private val args: DetailsFragmentArgs by navArgs()

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    // using a view model factory because I need to pass in some of my own arguments
    private lateinit var viewModelFactory: DetailsViewModelFactory
    private lateinit var viewModel: DetailsViewModel

    private var mediaId: Int? = null

    private var isMovie = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        mediaId = args.mediaItem.mediaId

        isMovie = args.mediaItem.isMovie
        Log.d("dt", "onCreateView: $isMovie")

        viewModelFactory = DetailsViewModelFactory(mediaId, args.mediaItem.isMovie)

        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailsViewModel::class.java)

        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)

        binding.lifecycleOwner = this

        binding.viewmodel = viewModel

        configureWatchlistToggleButton()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun configureWatchlistToggleButton() {
        // set listener
        binding.btnToggleWatchlist.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // add item to DB
                val entity = viewModel.mediaDetails.value?.asWatchlistEntity(isMovie)
                entity?.let {
                    CoroutineScope(Dispatchers.IO).launch {
                        MovieCatalogDatabase.getInstance().watchlistDao.insert( // TODO this database operation should be moved to repository
                            it
                        )
                    }
                }
            } else {
                // remove item from DB
                CoroutineScope(Dispatchers.IO).launch {
                    MovieCatalogDatabase.getInstance().watchlistDao.delete(
                        mediaId,
                        isMovie
                    )
                }
            }
        }
    }


}