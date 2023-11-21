package com.example.marvelapp.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.core.usecase.AddFavoriteUseCase
import com.example.core.usecase.CheckFavoriteUseCase
import com.example.core.usecase.GetCharactersCategoriesUseCase
import com.example.core.usecase.base.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    getCharactersCategoriesUseCase: GetCharactersCategoriesUseCase,
    addFavoriteUseCase: AddFavoriteUseCase,
    checkFavoriteUseCase: CheckFavoriteUseCase,
    coroutineDispatcher: CoroutineDispatchers
): ViewModel(){

    val categories = UIActionStateLiveData(
        coroutineDispatcher.main(),
        getCharactersCategoriesUseCase
    )

    val favorite = FavoriteUiActionStateLiveData(
        coroutineDispatcher.main(),
        checkFavoriteUseCase,
        addFavoriteUseCase
    )

}