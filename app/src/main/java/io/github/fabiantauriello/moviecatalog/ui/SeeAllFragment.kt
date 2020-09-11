package io.github.fabiantauriello.moviecatalog.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.map
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseService
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailGridBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.viewmodels.SeeAllFragmentViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class SeeAllFragment : Fragment(), ThumbnailClickListener {

    private var _binding: MediaThumbnailGridBinding? = null
    private val binding get() = _binding!!

    private val args: SeeAllFragmentArgs by navArgs()

    private val viewModel: SeeAllFragmentViewModel by viewModels()

    // switch between these two adapters as user chooses to search or not.
    private lateinit var allItemsAdapter: MediaGridAdapterPaged
    private lateinit var searchResultsAdapter: MediaGridAdapter

    private var showMovies = false

    private var job: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.media_thumbnail_grid, container, false)

        allItemsAdapter = MediaGridAdapterPaged(this)
        searchResultsAdapter = MediaGridAdapter(viewModel.searchResults.value, this)

        showMovies = args.endpoint.substringBefore("/") == "movie"

        // set up recycler view
        binding.rvList.adapter = allItemsAdapter

        initializePaging()
        configureSearchLiveDataObserver()

        return binding.root
    }

    private fun initializePaging() {
        job?.cancel()
        job = lifecycleScope.launch {
            viewModel.retrieveMediaItemsAsFlow(args.endpoint).collectLatest {
                allItemsAdapter.submitData(it.map {
                    MediaItem(
                        it.mediaId,
                        it.title,
                        it.posterPath,
                        showMovies
                    )
                })
            }
        }
    }

    private fun configureSearchLiveDataObserver() {
        viewModel.searchResults.observe(viewLifecycleOwner, Observer { searchResults ->
            if (searchResults != null) {
                binding.rvList.adapter = searchResultsAdapter
                (binding.rvList.adapter as MediaGridAdapter).updateData(searchResults)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        // Inflate the options menu from XML
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.action_bar_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }

        // set search hint
        val searchHint = "Search " + if (showMovies) "Movies" else "TV"
        searchView.queryHint = searchHint

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {

                    if (showMovies) {
                        viewModel.searchApi(TheMovieDatabaseService.SEARCH + "movie", query)
                    } else {
                        viewModel.searchApi(TheMovieDatabaseService.SEARCH + "tv", query)
                    }

                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        searchView.setOnCloseListener {
            // restore all items state
            binding.rvList.adapter = allItemsAdapter
            false
        }

        super.onCreateOptionsMenu(menu, menuInflater)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onThumbnailClick(mediaItem: MediaItem) {
        Log.d("TAG", "onThumbnailClick: $mediaItem")
        val action = SeeAllFragmentDirections.actionSeeAllFragmentToDetailsFragment(mediaItem)
        findNavController().navigate(action)
    }


}