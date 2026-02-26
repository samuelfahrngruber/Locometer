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

    Text("hasSpeed=${location?.hasSpeed()}, speed=${location?.speed}, location=$location")
}