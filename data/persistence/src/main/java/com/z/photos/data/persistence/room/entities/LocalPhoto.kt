package com.z.photos.data.persistence.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LocalPhoto(
    @PrimaryKey val id: Long,
    val photoUrl: String,
    val photoThumbnailUrl: String,
    val artist: String,
    val page: Int,
    val cachedAt: Long,
)
