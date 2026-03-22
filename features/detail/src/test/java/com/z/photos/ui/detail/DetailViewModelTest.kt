package com.z.photos.ui.detail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
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
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mock<PhotoRepository>()
    private val dispatchers = object : DispatcherProvider {
        override val io: CoroutineDispatcher = testDispatcher
        override val main: CoroutineDispatcher = testDispatcher
    }

    private val testPhoto = Photo(
        id = 1L,
        photoUrl = "url",
        isFavorite = false,
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
    fun `loads photo on init`() = runTest {
        `when`(repository.getPhoto(1L)).thenReturn(testPhoto)
        val savedStateHandle = SavedStateHandle(mapOf("photoId" to 1L))

        val viewModel = DetailViewModel(savedStateHandle, repository, dispatchers)

        assertEquals(testPhoto, viewModel.photoDetailsState.value)
    }

    @Test
    fun `toggleFavorite favorites an unfavorited photo`() = runTest {
        `when`(repository.getPhoto(1L)).thenReturn(testPhoto)
        val savedStateHandle = SavedStateHandle(mapOf("photoId" to 1L))
        val viewModel = DetailViewModel(savedStateHandle, repository, dispatchers)

        viewModel.photoDetailsState.test {
            assertEquals(testPhoto, awaitItem())

            viewModel.toggleFavorite()

            assertEquals(true, awaitItem()?.isFavorite)
            verify(repository).favoritePhoto(1L)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite unfavorites a favorited photo`() = runTest {
        `when`(repository.getPhoto(1L)).thenReturn(testPhoto.copy(isFavorite = true))
        val savedStateHandle = SavedStateHandle(mapOf("photoId" to 1L))
        val viewModel = DetailViewModel(savedStateHandle, repository, dispatchers)

        viewModel.photoDetailsState.test {
            assertEquals(true, awaitItem()?.isFavorite)

            viewModel.toggleFavorite()

            assertEquals(false, awaitItem()?.isFavorite)
            verify(repository).unfavoritePhoto(1L)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite does nothing when photo is null`() = runTest {
        `when`(repository.getPhoto(1L)).thenReturn(null)
        val savedStateHandle = SavedStateHandle(mapOf("photoId" to 1L))
        val viewModel = DetailViewModel(savedStateHandle, repository, dispatchers)

        viewModel.toggleFavorite()

        assertNull(viewModel.photoDetailsState.value)
    }
}
