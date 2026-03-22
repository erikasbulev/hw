package com.z.photos.data.repository

import com.z.photos.data.datasource.LocalDataSource
import com.z.photos.data.datasource.RemoteDataSource
import com.z.photos.domain.entities.Photo
import com.z.photos.data.persistence.time.TimeProvider
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class PhotoRepositoryImplTest {

    private val localDataSource = mock<LocalDataSource>()
    private val remoteDataSource = mock<RemoteDataSource>()
    private val timeProvider = mock<TimeProvider>()

    private val fixture = PhotoRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        timeProvider = timeProvider,
    )

    private val testPhoto = Photo(
        id = 1,
        photoUrl = "url",
        isFavorite = false,
        photoThumbnailUrl = "thumb_url",
        artist = "artist",
    )

    @Test
    fun `getRemotePhotos delegates to remote data source`() = runTest {
        val photos = listOf(testPhoto)
        `when`(remoteDataSource.getPhotos(1)).thenReturn(photos)

        val result = fixture.getRemotePhotos(1)

        verify(remoteDataSource).getPhotos(1)
        assertEquals(photos, result)
    }

    @Test
    fun `getLocalPhotos delegates to local data source`() = runTest {
        val photos = listOf(testPhoto)
        `when`(localDataSource.getPhotos(1)).thenReturn(photos)

        val result = fixture.getLocalPhotos(1)

        verify(localDataSource).getPhotos(1)
        assertEquals(photos, result)
    }

    @Test
    fun `getPhoto returns local photo when available`() = runTest {
        `when`(localDataSource.getPhoto(1L)).thenReturn(testPhoto)

        val result = fixture.getPhoto(1L)

        assertEquals(testPhoto, result)
    }

    @Test
    fun `getPhoto falls back to remote when local is null`() = runTest {
        val remotePhoto = testPhoto.copy(isFavorite = false)
        `when`(localDataSource.getPhoto(1L)).thenReturn(null)
        `when`(remoteDataSource.getPhoto(1L)).thenReturn(remotePhoto)
        `when`(localDataSource.isFavorite(1L)).thenReturn(true)

        val result = fixture.getPhoto(1L)

        assertEquals(true, result.isFavorite)
    }

    @Test
    fun `isCacheStale returns true when no timestamp`() = runTest {
        `when`(localDataSource.getCacheTimestamp(1)).thenReturn(null)

        assertTrue(fixture.isCacheStale(1))
    }

    @Test
    fun `isCacheStale returns false when cache is fresh`() = runTest {
        `when`(localDataSource.getCacheTimestamp(1)).thenReturn(1000L)
        `when`(timeProvider.currentTimeMillis()).thenReturn(2000L)

        assertFalse(fixture.isCacheStale(1))
    }

    @Test
    fun `isCacheStale returns true when cache is older than 5 minutes`() = runTest {
        `when`(localDataSource.getCacheTimestamp(1)).thenReturn(1000L)
        `when`(timeProvider.currentTimeMillis()).thenReturn(1000L + 5 * 60 * 1000L + 1)

        assertTrue(fixture.isCacheStale(1))
    }

    @Test
    fun `clearPhotos delegates to local data source`() = runTest {
        fixture.clearPhotos()

        verify(localDataSource).clearPhotos()
    }
}
