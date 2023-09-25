package com.example.marvelapp.framework.di

import com.example.core.usecase.GetCharactersUseCase
import com.example.core.usecase.GetCharactersUseCaseImpl
import com.example.core.usecase.GetCharactersCategoriesUseCase
import com.example.core.usecase.GetCharactersCategoriesUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindCharactersUseCase(useCase: GetCharactersUseCaseImpl) : GetCharactersUseCase

    @Binds
    fun bindGetComicsUseCase(useCase: GetCharactersCategoriesUseCaseImpl) : GetCharactersCategoriesUseCase

}