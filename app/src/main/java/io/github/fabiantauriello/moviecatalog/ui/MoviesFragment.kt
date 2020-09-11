package io.github.fabiantauriello.moviecatalog.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseService
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailCategoriesBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.util.Utils
import io.github.fabiantauriello.moviecatalog.viewmodels.MoviesViewModel

/**
 * fragment showing different movies under specific categories
 */
class MoviesFragment : Fragment(), View.OnClickListener, ThumbnailClickListener {

    private var _binding: MediaThumbnailCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "Movies"

        _binding =
            DataBindingUtil.inflate(inflater, R.layout.media_thumbnail_categories, container, false)

        // Allows Data Binding to Observe LiveData with the lifecycle of this Fragment
        binding.lifecycleOwner = this

        configureCategories()
        configureRecyclerViews()
        configureLiveDataObservers()
        configureClickListeners()

        return binding.root
    }

    private fun configureCategories() {
        binding.textCategory1.text = getString(R.string.category_title_latest)
        binding.textCategory2.text = getString(R.string.category_title_now_playing)
        binding.textCategory3.text = getString(R.string.category_title_popular)
        binding.textCategory4.text = getString(R.string.category_title_top_rated)
        binding.textCategory5.text = getString(R.string.category_title_upcoming)
    }

    private fun configureRecyclerViews() {
        binding.rvCategory2.adapter = MediaCategoryAdapter(viewModel.moviesNowPlaying.value, this)
        binding.rvCategory3.adapter = MediaCategoryAdapter(viewModel.moviesPopular.value, this)
        binding.rvCategory4.adapter = MediaCategoryAdapter(viewModel.moviesTopRated.value, this)
        binding.rvCategory5.adapter = MediaCategoryAdapter(viewModel.moviesUpcoming.value, this)
    }

    private fun configureLiveDataObservers() {
        // observe latest LiveData
        viewModel.movieLatest.observe(viewLifecycleOwner, Observer { movieLatest ->
            binding.incCategory1.textTitle.text = movieLatest.title
            Utils.setThumbnailImage(
                movieLatest.posterPath,
                binding.incCategory1.imagePoster
            )
        })

        // observe now playing LiveData
        viewModel.moviesNowPlaying.observe(viewLifecycleOwner, Observer { moviesList ->
            (binding.rvCategory2.adapter as MediaCategoryAdapter).updateData(moviesList)
        })

        // observe popular LiveData
        viewModel.moviesPopular.observe(viewLifecycleOwner, Observer { moviesList ->
            (binding.rvCategory3.adapter as MediaCategoryAdapter).updateData(moviesList)
        })

        // observe top rated LiveData
        viewModel.moviesTopRated.observe(viewLifecycleOwner, Observer { moviesList ->
            (binding.rvCategory4.adapter as MediaCategoryAdapter).updateData(moviesList)
        })

        // observe upcoming LiveData
        viewModel.moviesUpcoming.observe(viewLifecycleOwner, Observer { moviesList ->
            (binding.rvCategory5.adapter as MediaCategoryAdapter).updateData(moviesList)
        })
    }

    private fun configureClickListeners() {
        binding.incCategory1.card.setOnClickListener {
            viewModel.movieLatest.value?.let {
                val action =
                    MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(
                        it
                    )
                findNavController().navigate(action)
            }
        }
        binding.textCat2SeeAll.setOnClickListener(this)
        binding.textCat3SeeAll.setOnClickListener(this)
        binding.textCat4SeeAll.setOnClickListener(this)
        binding.textCat5SeeAll.setOnClickListener(this)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onClick(view: View?) {
        val endpoint = when (view?.id) {
            R.id.text_cat_2_see_all -> TheMovieDatabaseService.MOVIE_NOW_PLAYING
            R.id.text_cat_3_see_all -> TheMovieDatabaseService.MOVIE_POPULAR
            R.id.text_cat_4_see_all -> TheMovieDatabaseService.MOVIE_TOP_RATED
            R.id.text_cat_5_see_all -> TheMovieDatabaseService.MOVIE_UPCOMING
            else -> throw IllegalStateException("Failed to assign an endpoint")
        }
        val action = MoviesFragmentDirections.actionMoviesFragmentToSeeAllFragment(endpoint)
        findNavController().navigate(action)
    }

    override fun onThumbnailClick(mediaItem: MediaItem) {
        val action = MoviesFragmentDirections.actionMoviesFragmentToDetailsFragment(mediaItem)
        findNavController().navigate(action)
    }


}