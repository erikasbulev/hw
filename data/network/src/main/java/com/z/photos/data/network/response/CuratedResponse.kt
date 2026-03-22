package com.z.photos.data.network.response

import com.z.photos.data.network.entities.PhotoEntity

data class CuratedResponse(
    val page: Int,
    val perPage: Int,
    val photos: List<PhotoEntity>,
    val nextPage: String?,
)
