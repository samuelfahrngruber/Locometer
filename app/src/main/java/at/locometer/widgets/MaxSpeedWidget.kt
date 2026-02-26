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
fun MaxSpeedWidget(
    liveInfo: LiveInfo
) {
    val maxSpeedKmh by liveInfo.maxSpeedKmh.collectAsState(-1)

    Text("Max speed: $maxSpeedKmh")
}
