package com.z.photos.persistence.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FavoritePhoto(
    @PrimaryKey val id: Long,
)
