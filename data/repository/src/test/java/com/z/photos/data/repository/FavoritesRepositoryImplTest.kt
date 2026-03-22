package com.z.photos.data.repository

import com.z.photos.data.network.datasource.RemoteDataSource
import com.z.photos.data.persistence.datasource.LocalDataSource
import com.z.photos.domain.entities.Photo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class FavoritesRepositoryImplTest {

    private val localDataSource = mock<LocalDataSource>()
    private val remoteDataSource = mock<RemoteDataSource>()
    private val favoriteChangeNotifier = mock<FavoriteChangeNotifier>()

    private val fixture = FavoritesRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        favoriteChangeNotifier = favoriteChangeNotifier,
    )

    private val testPhoto = Photo(
        id = 1,
        photoUrl = "url",
        isFavorite = false,
        photoThumbnailUrl = "thumb_url",
        artist = "artist",
    )

    @Test
    fun `favoritePhoto delegates and notifies`() = runTest {
        fixture.favoritePhoto(1L)

        verify(localDataSource).favorite(1L)
        verify(favoriteChangeNotifier).notifyChange()
    }

    @Test
    fun `unfavoritePhoto delegates and notifies`() = runTest {
        fixture.unfavoritePhoto(1L)

        verify(localDataSource).unfavorite(1L)
        verify(favoriteChangeNotifier).notifyChange()
    }

    @Test
    fun `getFavoritePhotos returns local favorites when available`() = runTest {
        val favorites = listOf(testPhoto.copy(isFavorite = true))
        `when`(localDataSource.getFavoritePhotos()).thenReturn(favorites)

        val result = fixture.getFavoritePhotos()

        assertEquals(favorites, result)
    }

    @Test
    fun `getFavoritePhotos falls back to remote when local is empty`() = runTest {
        `when`(localDataSource.getFavoritePhotos()).thenReturn(emptyList())
        `when`(localDataSource.getFavoriteIds()).thenReturn(listOf(1L))
        `when`(remoteDataSource.getPhoto(1L)).thenReturn(testPhoto)

        val result = fixture.getFavoritePhotos()

        assertEquals(1, result.size)
        assertEquals(true, result[0].isFavorite)
    }

    @Test
    fun `getFavoriteCount delegates to local data source`() = runTest {
        `when`(localDataSource.getFavoriteCount()).thenReturn(5)

        val result = fixture.getFavoriteCount()

        assertEquals(5, result)
    }
}
