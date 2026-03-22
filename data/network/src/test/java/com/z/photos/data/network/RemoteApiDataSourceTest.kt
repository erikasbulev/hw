package com.z.photos.data.network

import com.z.photos.data.network.api.PixelsApi
import com.z.photos.data.network.entities.PhotoEntity
import com.z.photos.data.network.entities.PhotoSource
import com.z.photos.data.network.response.CuratedResponse
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import retrofit2.HttpException
import retrofit2.Response

class RemoteApiDataSourceTest {

    private val api = mock<PixelsApi>()
    private val fixture = RemoteApiDataSource(api)

    @Test
    fun `getPhotos maps api response to domain photos`() = runTest {
        val photoEntity = PhotoEntity(
            id = 1L,
            url = "url",
            photographer = "artist",
            photoSource = PhotoSource(
                original = "original_url",
                small = "small_url",
                medium = "medium_url",
                tiny = "tiny_url",
            ),
        )
        val response = CuratedResponse(
            page = 1,
            perPage = 20,
            photos = listOf(photoEntity),
            nextPage = "next",
        )
        `when`(api.getCuratedPhotos(perPage = 20, page = 1)).thenReturn(response)

        val result = fixture.getPhotos(1)

        assertEquals(1, result.size)
        assertEquals(1L, result[0].id)
        assertEquals("original_url", result[0].photoUrl)
        assertEquals("tiny_url", result[0].photoThumbnailUrl)
        assertEquals("artist", result[0].artist)
        assertEquals(false, result[0].isFavorite)
    }

    @Test
    fun `getPhotos returns empty list on HttpException`() = runTest {
        val httpException = HttpException(Response.error<Any>(401, okhttp3.ResponseBody.create(null, "")))
        `when`(api.getCuratedPhotos(perPage = 20, page = 1)).thenThrow(httpException)

        val result = fixture.getPhotos(1)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getPhoto maps api response to domain photo`() = runTest {
        val photoEntity = PhotoEntity(
            id = 5L,
            url = "url",
            photographer = "photographer",
            photoSource = PhotoSource(
                original = "original",
                small = "small",
                medium = "medium",
                tiny = "tiny",
            ),
        )
        `when`(api.getPhoto(5L)).thenReturn(photoEntity)

        val result = fixture.getPhoto(5L)

        verify(api).getPhoto(5L)
        assertEquals(5L, result.id)
        assertEquals("photographer", result.artist)
    }
}
