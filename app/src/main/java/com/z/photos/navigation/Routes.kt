package com.z.photos.navigation

const val PHOTO_ID_ARG = "photoId"

object Routes {
    const val FEED = "feed"
    const val DETAIL = "detail/{$PHOTO_ID_ARG}"

    fun detail(photoId: Long) = "detail/$photoId"
}
