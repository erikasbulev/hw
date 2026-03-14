package com.z.photos.business.repositories

import com.z.photos.business.datasources.LocalDataSource
import com.z.photos.business.datasources.RemoteDataSource
import com.z.photos.business.entities.Photo
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`

class PhotoRepositoryImplTest {

    private val localDataSource = mock<LocalDataSource>()
    private val remoteDataSource = mock<RemoteDataSource>()

    private val fixture = PhotoRepositoryImpl(
        localDataSource = localDataSource,
        remoteDataSource = remoteDataSource
    )

    @Test
    fun `get remote photos when local source is null`() = runTest {
        val page = 1
        val remoteData = listOf(
            Photo(
                id = 1,
                photUrl = "url",
                isFavorite = false,
                photoThumbnailUrl = "thumb_url",
                artist = "artist"
            )
        )
        `when`(localDataSource.getPhotos(page)).thenReturn(null)
        `when`(remoteDataSource.getPhotos(page)).thenReturn(remoteData)

        val actualData = fixture.getPhotos(page)

        verify(remoteDataSource).getPhotos(page)
        assertEquals(remoteData, actualData)
    }
}