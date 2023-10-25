package com.example.core.usecase

import com.example.core.data.repository.CharactersRepository
import com.example.core.usecase.base.ResultStatus
import com.example.test.MainCoroutineRule
import com.example.test.model.CharacterFactory
import com.example.test.model.ComicFactory
import com.example.test.model.EventFactory
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetCharactersCategoriesUseCaseImplTest{

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: CharactersRepository

    private val character = CharacterFactory().create(CharacterFactory.Hero.ThreeDMan)
    private val comics = listOf(ComicFactory().create(ComicFactory.FakeComic.FakeComic1))
    private val events = listOf(EventFactory().create(EventFactory.FakeEvent.FakeEvent1))

    private lateinit var getCharacterCategoriesUseCase: GetCharactersCategoriesUseCase

    @Before
    fun setUp(){
        getCharacterCategoriesUseCase = GetCharactersCategoriesUseCaseImpl(
            repository,
            mainCoroutineRule.testDispatcherProvider
        )
    }

    @Test
    fun `should return Success from ResultStatus when get both requests return success`() =
        runTest {
            // Arrange
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenReturn(events)

            // Act
            val result = getCharacterCategoriesUseCase
                .invoke(GetCharactersCategoriesUseCase.GetCharacterCategoriesParams(character.id))

            // Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Success)
        }

    @Test
    fun `should return Error from ResultStatus when get events request returns error`() =
        runTest {
            whenever(repository.getComics(character.id)).thenReturn(comics)
            whenever(repository.getEvents(character.id)).thenAnswer { throw Throwable() }

            // Act
            val result = getCharacterCategoriesUseCase
                .invoke(GetCharactersCategoriesUseCase.GetCharacterCategoriesParams(character.id))

            // Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }

    @Test
    fun `should return Error from ResultStatus when get comics request returns error`() =
        runTest {
            whenever(repository.getComics(character.id)).thenAnswer{throw Throwable()}

            // Act
            val result = getCharacterCategoriesUseCase
                .invoke(GetCharactersCategoriesUseCase.GetCharacterCategoriesParams(character.id))

            // Assert
            val resultList = result.toList()
            assertEquals(ResultStatus.Loading, resultList[0])
            assertTrue(resultList[1] is ResultStatus.Error)
        }

}