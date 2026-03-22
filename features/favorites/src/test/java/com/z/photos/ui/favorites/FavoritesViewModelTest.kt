package com.z.photos.ui.favorites

import com.z.photos.data.repository.FavoriteChangeNotifier
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.FavoritesRepository
import com.z.photos.ui.core.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val favoritesRepository = mock<FavoritesRepository>()
    private val favoriteChangeNotifier = FavoriteChangeNotifier()
    private val dispatchers = object : DispatcherProvider {
        override val io: CoroutineDispatcher = testDispatcher
        override val main: CoroutineDispatcher = testDispatcher
    }

    private val testPhoto = Photo(
        id = 1L,
        photoUrl = "url",
        isFavorite = true,
        photoThumbnailUrl = "thumb",
        artist = "artist",
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads favorites on init`() = runTest {
        val favorites = listOf(testPhoto)
        `when`(favoritesRepository.getFavoritePhotos()).thenReturn(favorites)

        val viewModel = FavoritesViewModel(favoritesRepository, favoriteChangeNotifier, dispatchers)

        assertEquals(favorites, viewModel.favorites.value)
    }

    @Test
    fun `unfavorite delegates to repository`() = runTest {
        `when`(favoritesRepository.getFavoritePhotos()).thenReturn(listOf(testPhoto))
        val viewModel = FavoritesViewModel(favoritesRepository, favoriteChangeNotifier, dispatchers)

        viewModel.unfavorite(testPhoto)

        verify(favoritesRepository).unfavoritePhoto(testPhoto.id)
    }
}
