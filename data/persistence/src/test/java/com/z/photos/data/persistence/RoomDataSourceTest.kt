package com.z.photos.data.persistence

import com.z.photos.data.persistence.room.dao.FavoritePhotoDao
import com.z.photos.data.persistence.room.dao.LocalPhotoDao
import com.z.photos.data.persistence.room.entities.FavoritePhoto
import com.z.photos.data.persistence.room.entities.LocalPhoto
import com.z.photos.data.persistence.room.entities.LocalPhotoWithFavorite
import com.z.photos.data.persistence.time.TimeProvider
import com.z.photos.domain.entities.Photo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class RoomDataSourceTest {

    private val localPhotoDao = mock<LocalPhotoDao>()
    private val favoritePhotoDao = mock<FavoritePhotoDao>()
    private val timeProvider = mock<TimeProvider>()

    private val fixture = RoomDataSource(
        localPhotoDao = localPhotoDao,
        favoritePhotoDao = favoritePhotoDao,
        timeProvider = timeProvider,
    )

    @Test
    fun `getPhotos maps local photos to domain photos`() = runTest {
        val localPhotos = listOf(
            LocalPhotoWithFavorite(
                id = 1L,
                photoUrl = "url",
                photoThumbnailUrl = "thumb",
                artist = "artist",
                page = 1,
                cachedAt = 1000L,
                isFavorite = true,
            )
        )
        `when`(localPhotoDao.getPhotos(1)).thenReturn(localPhotos)

        val result = fixture.getPhotos(1)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("url", result[0].photoUrl)
        assertEquals(true, result[0].isFavorite)
    }

    @Test
    fun `getPhoto returns null when not found`() = runTest {
        `when`(localPhotoDao.getPhotoById(99L)).thenReturn(null)

        val result = fixture.getPhoto(99L)

        assertNull(result)
    }

    @Test
    fun `persistPhotos uses time provider for cachedAt`() = runTest {
        `when`(timeProvider.currentTimeMillis()).thenReturn(5000L)
        val photos = listOf(
            Photo(
                id = 1L,
                photoUrl = "url",
                isFavorite = false,
                photoThumbnailUrl = "thumb",
                artist = "artist",
            )
        )

        fixture.persistPhotos(1, photos)

        val expected = listOf(
            LocalPhoto(
                id = 1L,
                photoUrl = "url",
                photoThumbnailUrl = "thumb",
                artist = "artist",
                page = 1,
                cachedAt = 5000L,
            )
        )
        verify(localPhotoDao).insertPhotos(expected)
    }

    @Test
    fun `favorite inserts favorite photo`() = runTest {
        fixture.favorite(1L)

        verify(favoritePhotoDao).insertFavorite(FavoritePhoto(1L))
    }

    @Test
    fun `unfavorite deletes favorite photo`() = runTest {
        fixture.unfavorite(1L)

        verify(favoritePhotoDao).deleteFavorite(FavoritePhoto(1L))
    }

    @Test
    fun `getCacheTimestamp returns null when no photos`() = runTest {
        `when`(localPhotoDao.getPhotos(1)).thenReturn(emptyList())

        val result = fixture.getCacheTimestamp(1)

        assertNull(result)
    }

    @Test
    fun `getCacheTimestamp returns cachedAt of first photo`() = runTest {
        val localPhotos = listOf(
            LocalPhotoWithFavorite(
                id = 1L,
                photoUrl = "url",
                photoThumbnailUrl = "thumb",
                artist = "artist",
                page = 1,
                cachedAt = 3000L,
                isFavorite = false,
            )
        )
        `when`(localPhotoDao.getPhotos(1)).thenReturn(localPhotos)

        val result = fixture.getCacheTimestamp(1)

        assertEquals(3000L, result)
    }

    @Test
    fun `clearPhotos delegates to dao`() = runTest {
        fixture.clearPhotos()

        verify(localPhotoDao).clearPhotos()
    }

    @Test
    fun `getFavoriteCount delegates to dao`() = runTest {
        `when`(favoritePhotoDao.getFavoriteCount()).thenReturn(5)

        val result = fixture.getFavoriteCount()

        assertEquals(5, result)
    }

    @Test
    fun `getFavoriteIds delegates to dao`() = runTest {
        `when`(favoritePhotoDao.getFavoriteIds()).thenReturn(listOf(1L, 2L))

        val result = fixture.getFavoriteIds()

        assertEquals(listOf(1L, 2L), result)
    }

    @Test
    fun `isFavorite delegates to dao`() = runTest {
        `when`(favoritePhotoDao.isFavorite(1L)).thenReturn(true)

        val result = fixture.isFavorite(1L)

        assertTrue(result)
    }
}
