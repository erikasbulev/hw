package com.z.photos.domain.entities

data class Photo(
    val id: Long,
    val photoUrl: String,
    val isFavorite: Boolean,
    val photoThumbnailUrl: String,
    val artist: String,
)
