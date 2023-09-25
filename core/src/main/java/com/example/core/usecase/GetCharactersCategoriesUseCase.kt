package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.domain.model.Comic
import com.example.core.domain.model.Event
import com.example.core.usecase.base.AppCoroutinesDispatchers
import com.example.core.usecase.base.ResultStatus
import com.example.core.usecase.base.UseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

interface GetCharactersCategoriesUseCase {

    operator fun invoke(params: GetCharacterCategoriesParams): Flow<ResultStatus<Pair<List<Comic>, List<Event>>>>

    data class GetCharacterCategoriesParams(val characterId: Int)

}

class GetCharactersCategoriesUseCaseImpl @Inject constructor(
    private val repository: CharactersRepository,
    private val dispatcher: AppCoroutinesDispatchers
): GetCharactersCategoriesUseCase,
    UseCase<GetCharactersCategoriesUseCase.GetCharacterCategoriesParams, Pair<List<Comic>, List<Event>>>(){
    override suspend fun doWork(
        params: GetCharactersCategoriesUseCase.GetCharacterCategoriesParams
    ): ResultStatus<Pair<List<Comic>, List<Event>>> {
        return withContext(dispatcher.io){
            val comicsDeferred = async{ repository.getComics(params.characterId) }
            val eventsDeferred = async{ repository.getEvents(params.characterId) }

            val comics = comicsDeferred.await()
            val events = eventsDeferred.await()

            ResultStatus.Success(comics to events)
        }
    }

}