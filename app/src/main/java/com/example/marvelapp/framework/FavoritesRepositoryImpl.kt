package com.example.marvelapp.framework

import com.example.core.data.repository.FavoritesLocalDataSource
import com.example.core.data.repository.FavoritesRepository
import com.example.core.domain.model.Character
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDatasSource: FavoritesLocalDataSource
) : FavoritesRepository {

    override fun getAll(): Flow<List<Character>> {
        return favoritesLocalDatasSource.getAll()
    }

    override suspend fun isFavorite(characterId: Int): Boolean {
        return favoritesLocalDatasSource.isFavorite(characterId)
    }

    override suspend fun saveFavorite(character: Character) {
        return favoritesLocalDatasSource.save(character)
    }

    override suspend fun deleteFavorite(character: Character) {
        return favoritesLocalDatasSource.delete(character)
    }
}