package com.z.photos.data.persistence.room.entities

data class LocalPhotoWithFavorite(
    val id: Long,
    val photoUrl: String,
    val photoThumbnailUrl: String,
    val artist: String,
    val page: Int,
    val isFavorite: Boolean,
)
