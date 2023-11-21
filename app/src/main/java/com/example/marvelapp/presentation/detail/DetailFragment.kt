package com.example.marvelapp.presentation.detail

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.marvelapp.databinding.FragmentDetailBinding
import com.example.marvelapp.framework.imageloader.ImageLoader
import com.example.marvelapp.presentation.extensions.showShortToast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    private val binding : FragmentDetailBinding get() = _binding!!

    private val args by navArgs<DetailFragmentArgs>()

    private val viewModel: DetailViewModel by viewModels()

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDetailBinding.inflate(
        inflater,
        container,
        false
    ).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val detailViewArg = args.detailViewArg
        binding.imageCharacter.run{
            transitionName = detailViewArg.name
            imageLoader.load(this, detailViewArg.imageUrl)
        }

        setSharedElementTransitionOnEnter()

        loadCategoriesAndObserveUiState(detailViewArg)
        setAndObserveFavoriteUiState(detailViewArg)
    }

    private fun loadCategoriesAndObserveUiState(detailViewArg: DetailViewArg) {
        viewModel.categories.load(detailViewArg.characterId)
        viewModel.categories.state.observe(viewLifecycleOwner){ uiState ->
            binding.flipperDetail.displayedChild = when(uiState){
                UIActionStateLiveData.UiState.Loading -> FLIPPER_CHILD_POSITION_LOADING
                is UIActionStateLiveData.UiState.Success -> {
                    binding.recyclerParentDetail.run{
                        setHasFixedSize(true)
                        adapter = DetailParentAdapter(uiState.detailParentList, imageLoader)
                    }
                    FLIPPER_CHILD_POSITION_DETAIL
                }
                is UIActionStateLiveData.UiState.Empty -> FLIPPER_CHILD_POSITION_EMPTY
                is UIActionStateLiveData.UiState.Error -> {
                    binding.includeErrorView.buttonRetry.setOnClickListener {
                        viewModel.categories.load(detailViewArg.characterId)
                    }
                    FLIPPER_CHILD_POSITION_ERROR
                }
            }
        }
    }

    private fun setAndObserveFavoriteUiState(detailViewArg: DetailViewArg){
        viewModel.favorite.run{
            checkFavorite(detailViewArg.characterId)

            binding.imageFavoriteIcon.setOnClickListener {
                update(detailViewArg)
            }

            viewModel.favorite.state.observe(viewLifecycleOwner){ uiState ->
                binding.flipperFavorite.displayedChild = when (uiState){
                    FavoriteUiActionStateLiveData.UiState.Loading -> FLIPPER_FAVORITE_CHILD_POSITION_LOADING
                    is FavoriteUiActionStateLiveData.UiState.Icon -> {
                        binding.imageFavoriteIcon.setImageResource(uiState.icon)
                        FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                    }
                    is FavoriteUiActionStateLiveData.UiState.Error -> {
                        showShortToast(uiState.message)
                        FLIPPER_FAVORITE_CHILD_POSITION_IMAGE
                    }
                }
            }
        }
    }

    private fun setSharedElementTransitionOnEnter(){
        TransitionInflater.from(requireContext())
            .inflateTransition(android.R.transition.move).apply{
                sharedElementEnterTransition = this
            }
    }

    companion object{
        private const val FLIPPER_CHILD_POSITION_LOADING = 0
        private const val FLIPPER_CHILD_POSITION_DETAIL = 1
        private const val FLIPPER_CHILD_POSITION_ERROR = 2
        private const val FLIPPER_CHILD_POSITION_EMPTY = 3
        private const val FLIPPER_FAVORITE_CHILD_POSITION_IMAGE = 0
        private const val FLIPPER_FAVORITE_CHILD_POSITION_LOADING = 1
    }
}