package com.z.photos.business.entities

data class Photo(
    val id: Long,
    val photUrl: String,
    val isFavorite: Boolean,
    val photoThumbnailUrl: String,
    val artist: String,
)