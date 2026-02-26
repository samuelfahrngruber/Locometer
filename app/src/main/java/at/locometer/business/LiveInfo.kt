package at.locometer.business

import at.locometer.dataaccess.AndroidLocationRepository
import at.locometer.dataaccess.OverpassApiRepository
import kotlinx.coroutines.flow.map

class LiveInfo(
    androidLocationRepository: AndroidLocationRepository,
    overpassApiRepository: OverpassApiRepository
) {
    val fullLocation = androidLocationRepository.location

    val hasSpeed = fullLocation.map { it.hasSpeed() }

    val speedMs = fullLocation.map { it.speed }

    val speedKmh = speedMs.map { it * 3.6f }

    val maxSpeedKmh = fullLocation.map {
        overpassApiRepository.queryTrainMaxSpeedKmh(it)
    }
}