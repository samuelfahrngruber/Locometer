package at.locometer.widgets

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import at.locometer.business.LiveInfo
import androidx.core.net.toUri

@Composable
@RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
fun LinksWidget(
    liveInfo: LiveInfo
) {

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OebbButton()
        OpenRailwayMapButton(liveInfo)
    }
}

@Composable
fun OpenRailwayMapButton(
    liveInfo: LiveInfo
) {
    val context = LocalContext.current
    val location by liveInfo.fullLocation.collectAsState(null)

    Button(
        enabled = location != null,
        onClick = {
            location?.let {
                val uri = "https://www.openrailwaymap.org/?style=standard&lat=${it.latitude}&lon=${it.longitude}&zoom=13&style=maxspeed"
                context.startActivity(Intent(Intent.ACTION_VIEW, uri.toUri()))
            }
        }
    ) {
        Text("OpenRailwayMap")
    }
}

@Composable
fun OebbButton() {
    val context = LocalContext.current

    Button(onClick = {
        val intent = context.packageManager.getLaunchIntentForPackage("at.oebb.ts")
            ?: Intent(Intent.ACTION_VIEW, "market://details?id=at.oebb.ts".toUri())
        context.startActivity(intent)
    }) {
        Text("ÖBB Tickets")
    }
}
