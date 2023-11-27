package com.example.marvelapp.presentation.favorites

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.switchMap
import com.example.core.usecase.GetFavoritesUseCase
import com.example.core.usecase.base.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val coroutineDispatchers: CoroutineDispatchers
) : ViewModel() {

    private val action = MutableLiveData<Action>()
    val state: LiveData<UiState> = action.switchMap { action ->
        liveData(coroutineDispatchers.main()) {
            when(action) {
                is Action.GetAll -> {
                    getFavoritesUseCase()
                        .catch{
                            emit(UiState.ShowEmpty)
                        }
                        .collect{
                            val items = it.map { character ->
                                FavoriteItem(
                                    character.id,
                                    character.name,
                                    character.imageUrl
                                )
                            }

                            val uiState = if(items.isEmpty()){
                                UiState.ShowEmpty
                            } else UiState.ShowFavorite(items)

                            emit(uiState)
                        }
                }
            }
        }
    }

    fun getAll(){
        action.value = Action.GetAll
    }

    sealed class UiState{
        data class ShowFavorite(val favorites: List<FavoriteItem>) : UiState()
        object ShowEmpty : UiState()
    }

    sealed class Action {
        object GetAll : Action()
    }

}