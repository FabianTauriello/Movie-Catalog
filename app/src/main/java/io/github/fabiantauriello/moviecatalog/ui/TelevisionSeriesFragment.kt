package io.github.fabiantauriello.moviecatalog.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import io.github.fabiantauriello.moviecatalog.R
import io.github.fabiantauriello.moviecatalog.data.network.TheMovieDatabaseService
import io.github.fabiantauriello.moviecatalog.databinding.MediaThumbnailCategoriesBinding
import io.github.fabiantauriello.moviecatalog.domain.MediaItem
import io.github.fabiantauriello.moviecatalog.util.Utils
import io.github.fabiantauriello.moviecatalog.viewmodels.TelevisionSeriesViewModel

/**
 * fragment showing different tv under specific categories
 */
class TelevisionSeriesFragment : Fragment(), View.OnClickListener, ThumbnailClickListener {

    private var _binding: MediaThumbnailCategoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TelevisionSeriesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.title = "TV"
        
        _binding = DataBindingUtil.inflate(inflater, R.layout.media_thumbnail_categories, container, false)

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
        binding.textCategory2.text = getString(R.string.category_title_airing_today)
        binding.textCategory3.text = getString(R.string.category_title_on_the_air)
        binding.textCategory4.text = getString(R.string.category_title_popular)
        binding.textCategory5.text = getString(R.string.category_title_top_rated)
    }

    private fun configureRecyclerViews() {
        binding.rvCategory2.adapter = MediaCategoryAdapter(viewModel.tvAiringToday.value, this)
        binding.rvCategory3.adapter = MediaCategoryAdapter(viewModel.tvOnTheAir.value, this)
        binding.rvCategory4.adapter = MediaCategoryAdapter(viewModel.tvPopular.value, this)
        binding.rvCategory5.adapter = MediaCategoryAdapter(viewModel.tvTopRated.value, this)
    }

    private fun configureLiveDataObservers() {
        // observe latest LiveData
        viewModel.tvLatest.observe(viewLifecycleOwner,  { tvLatest ->
            binding.incCategory1.textTitle.text = tvLatest.title
            Utils.setThumbnailImage(tvLatest.posterPath, binding.incCategory1.imagePoster)
        })

        // observe airing today LiveData
        viewModel.tvAiringToday.observe(viewLifecycleOwner,  { tvShowList ->
            (binding.rvCategory2.adapter as MediaCategoryAdapter).updateData(tvShowList)
        })

        // observe on the air LiveData
        viewModel.tvOnTheAir.observe(viewLifecycleOwner,  { tvShowList ->
            (binding.rvCategory3.adapter as MediaCategoryAdapter).updateData(tvShowList)
        })

        // observe popular LiveData
        viewModel.tvPopular.observe(viewLifecycleOwner,  { tvShowList ->
            (binding.rvCategory4.adapter as MediaCategoryAdapter).updateData(tvShowList)
        })

        // observe top rated LiveData
        viewModel.tvTopRated.observe(viewLifecycleOwner, { tvShowList ->
            (binding.rvCategory5.adapter as MediaCategoryAdapter).updateData(tvShowList)
        })
    }

    private fun configureClickListeners() {
        binding.incCategory1.card.setOnClickListener {
            viewModel.tvLatest.value?.let {
                val action =
                    TelevisionSeriesFragmentDirections.actionTelevisionSeriesFragmentToDetailsFragment(
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
        val endpoint = when(view?.id) {
            R.id.text_cat_2_see_all -> TheMovieDatabaseService.TV_AIRING_TODAY
            R.id.text_cat_3_see_all -> TheMovieDatabaseService.TV_ON_THE_AIR
            R.id.text_cat_4_see_all -> TheMovieDatabaseService.TV_POPULAR
            R.id.text_cat_5_see_all -> TheMovieDatabaseService.TV_TOP_RATED
            else -> throw IllegalStateException("Failed to assign an endpoint")
        }
        val action = TelevisionSeriesFragmentDirections.actionTelevisionSeriesFragmentToSeeAllFragment(endpoint)
        findNavController().navigate(action)
    }

    override fun onThumbnailClick(mediaItem: MediaItem) {
        // media is optional so pass -1 if it's null
        val action = TelevisionSeriesFragmentDirections.actionTelevisionSeriesFragmentToDetailsFragment(mediaItem)
        findNavController().navigate(action)
    }

}