package at.locometer.widgets

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import at.locometer.business.LiveInfo

@Composable
@RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
fun DebugWidget(
    liveInfo: LiveInfo
) {
    val location by liveInfo.fullLocation.collectAsState(null)
    val nrApiCalls by liveInfo.nrApiCalls.collectAsState(0)
    val speedKmh by liveInfo.speedKmh.collectAsState(0f)

    Text("""
        location.hasSpeed=${location?.hasSpeed()},
        location.speed=${location?.speed},
        location.accuracy=${location?.accuracy},
        location.altitude=${location?.altitude},
        location.extras=${location?.extras},
        location=$location,
        ---
        speedKmh=$speedKmh,
        nrApiCalls=$nrApiCalls,
    """.trimIndent())
}