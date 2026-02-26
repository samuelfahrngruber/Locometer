package at.locometer.business

import at.locometer.dataaccess.AndroidLocationRepository
import kotlinx.coroutines.flow.map

class LiveInfo(
    androidLocationRepository: AndroidLocationRepository
) {
    val fullLocation = androidLocationRepository.location

    val hasSpeed = fullLocation.map { it.hasSpeed() }

    val speedMs = fullLocation.map { it.speed }

    val speedKmh = speedMs.map { it * 3.6f }
}