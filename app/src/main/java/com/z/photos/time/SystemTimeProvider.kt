package com.z.photos.time

import com.z.photos.domain.time.TimeProvider
import javax.inject.Inject

class SystemTimeProvider @Inject constructor() : TimeProvider {

    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
