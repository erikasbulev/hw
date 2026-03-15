package com.z.photos.persistence.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalPhoto(
    @PrimaryKey val id: Long,
    val photUrl: String,
    val isFavorite: Boolean,
    val photoThumbnailUrl: String,
    val artist: String,
    val page: Int,
)