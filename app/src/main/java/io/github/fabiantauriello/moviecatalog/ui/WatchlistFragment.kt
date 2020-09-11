package io.github.fabiantauriello.moviecatalog.ui

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailGridBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.viewmodels.WatchlistViewModel

class WatchlistFragment : Fragment(), ThumbnailClickListener {

    private var _binding: MediaThumbnailGridBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WatchlistViewModel by lazy {
        ViewModelProvider(this).get(WatchlistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "Watchlist"

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.media_thumbnail_grid, container, false)

        binding.lifecycleOwner = this

        val adapter = MediaGridAdapter(viewModel.watchlist.value, this)

        binding.rvList.adapter = adapter

        // observe watchlist LiveData
        viewModel.watchlist.observe(
            viewLifecycleOwner,
            (binding.rvList.adapter as MediaGridAdapter)::updateData
        )

        return binding.root
    }

    override fun onThumbnailClick(mediaItem: MediaItem) {
        val action = WatchlistFragmentDirections.actionWatchlistFragmentToDetailsFragment(mediaItem)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, menuInflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater)

        // Inflate the options menu from XML
        val inflater = activity?.menuInflater
        inflater?.inflate(R.menu.action_bar_menu, menu)

        // Get the SearchView and set the searchable configuration
        val searchManager = activity?.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(activity?.componentName))
        }

        searchView.queryHint = "Search your watchlist"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { (binding.rvList.adapter as MediaGridAdapter).filterList(query) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

    }

}