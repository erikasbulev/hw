package com.z.photos.domain.time

interface TimeProvider {

    fun currentTimeMillis(): Long
}
