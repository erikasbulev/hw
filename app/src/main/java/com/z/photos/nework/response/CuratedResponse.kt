package com.z.photos.nework.response

import com.z.photos.nework.entities.PhotoEntity

data class CuratedResponse(
    val page: Int,
    val perPage: Int,
    val photos: List<PhotoEntity>,
    val nextPage: String,
)