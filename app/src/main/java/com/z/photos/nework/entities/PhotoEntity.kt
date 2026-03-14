package com.z.photos.nework.entities

import com.google.gson.annotations.SerializedName

data class PhotoEntity(
    val id: Long,
    val url: String,
    val photographer: String,
    @SerializedName("src")
    val photoSource: PhotoSource,
)