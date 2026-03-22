package com.z.photos.data.persistence.time

interface TimeProvider {

    fun currentTimeMillis(): Long
}
