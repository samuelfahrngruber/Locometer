package at.locometer

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

class AndroidLocationAccess @RequiresPermission(allOf = [
    Manifest.permission.ACCESS_COARSE_LOCATION,
    Manifest.permission.ACCESS_FINE_LOCATION
]) constructor(
    private val context: Context,
    private val scope: CoroutineScope,
) {
    private val locationProvider =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.Builder(
        Priority.PRIORITY_HIGH_ACCURACY,
        1_000L
    ).build()

    @SuppressLint("MissingPermission")
    val location: SharedFlow<Location> = callbackFlow {
        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it) }
            }
        }
        locationProvider.requestLocationUpdates(locationRequest, callback, Looper.getMainLooper())
        awaitClose { locationProvider.removeLocationUpdates(callback) }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        replay = 1
    )

    val speedMs = location.map { it.speed }

    val speedKmh = speedMs.map { it * 3.6f }
}
