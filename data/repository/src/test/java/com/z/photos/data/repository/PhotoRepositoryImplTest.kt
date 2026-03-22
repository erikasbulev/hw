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

class PhotoRepositoryImplTest {

    private val localDataSource = mock<LocalDataSource>()
    private val remoteDataSource = mock<RemoteDataSource>()
    private val favoriteChangeNotifier = mock<FavoriteChangeNotifier>()

    private val fixture = PhotoRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource,
        favoriteChangeNotifier = favoriteChangeNotifier,
    )

    @Test
    fun `get remote photos when local source is null`() = runTest {
        val page = 1
        val remoteData = listOf(
            Photo(
                id = 1,
                photoUrl = "url",
                isFavorite = false,
                photoThumbnailUrl = "thumb_url",
                artist = "artist"
            )
        )
        `when`(localDataSource.getPhotos(page)).thenReturn(null)
        `when`(remoteDataSource.getPhotos(page)).thenReturn(remoteData)

        val actualData = fixture.getRemotePhotos(page)

        verify(remoteDataSource).getPhotos(page)
        assertEquals(remoteData, actualData)
    }
}
