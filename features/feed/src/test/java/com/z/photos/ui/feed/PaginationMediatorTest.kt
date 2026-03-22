package com.z.photos.ui.feed

import app.cash.turbine.test
import com.z.photos.domain.entities.Photo
import com.z.photos.domain.repositories.PhotoRepository
import com.z.photos.ui.core.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class PaginationMediatorTest {

    private val testDispatcher = UnconfinedTestDispatcher()
    private val repository = mock<PhotoRepository>()
    private val dispatchers = object : DispatcherProvider {
        override val io: CoroutineDispatcher = testDispatcher
        override val main: CoroutineDispatcher = testDispatcher
    }
    private val fixture = PaginationMediator(
        photoRepository = repository,
        dispatchers = dispatchers,
    )

    private val testPhoto = Photo(
        id = 1L,
        photoUrl = "url",
        isFavorite = false,
        photoThumbnailUrl = "thumb",
        artist = "artist",
    )

    @Test
    fun `emits cached photos when cache is fresh`() = runTest {
        val photos = listOf(testPhoto)
        `when`(repository.getLocalPhotos(1)).thenReturn(photos)
        `when`(repository.isCacheStale(1)).thenReturn(false)

        launch { fixture.requestPage(1) }

        fixture.getPhotosFlow().test {
            val result = awaitItem()
            assertEquals(photos, result.photos)
            assertTrue(result.hasMore)
            assertEquals(2, result.nextPage)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `fetches remote when cache is stale`() = runTest {
        val remotePhotos = listOf(testPhoto)
        val savedPhotos = listOf(testPhoto.copy(isFavorite = true))
        `when`(repository.getLocalPhotos(1)).thenReturn(emptyList(), savedPhotos)
        `when`(repository.isCacheStale(1)).thenReturn(true)
        `when`(repository.getRemotePhotos(1)).thenReturn(remotePhotos)

        launch { fixture.requestPage(1) }

        fixture.getPhotosFlow().test {
            val result = awaitItem()
            verify(repository).savePhotos(1, remotePhotos)
            assertTrue(result.hasMore)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `sets hasMore false when remote returns empty`() = runTest {
        `when`(repository.getLocalPhotos(1)).thenReturn(emptyList())
        `when`(repository.isCacheStale(1)).thenReturn(true)
        `when`(repository.getRemotePhotos(1)).thenReturn(emptyList())

        launch { fixture.requestPage(1) }

        fixture.getPhotosFlow().test {
            val result = awaitItem()
            assertFalse(result.hasMore)
            assertTrue(result.photos.isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `refresh clears photos and requests first page`() = runTest {
        val photos = listOf(testPhoto)
        `when`(repository.getLocalPhotos(1)).thenReturn(photos)
        `when`(repository.isCacheStale(1)).thenReturn(false)

        launch { fixture.refresh() }

        fixture.getPhotosFlow().test {
            val result = awaitItem()
            verify(repository).clearPhotos()
            assertEquals(photos, result.photos)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
