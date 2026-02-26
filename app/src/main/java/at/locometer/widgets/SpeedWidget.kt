package at.locometer.widgets

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import at.locometer.business.LiveInfo
import at.locometer.dataaccess.AndroidLocationRepository
import kotlin.math.roundToInt

@Composable
@RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
fun SpeedWidget(
    liveInfo: LiveInfo
) {
    val hasSpeed by liveInfo.hasSpeed.collectAsState(false)

    if (!hasSpeed) {
        return Text(text="<no speed data>")
    }

    val speed by liveInfo.speedKmh.collectAsState(0f)

    Speedometer(speed)
}

@Composable
fun Speedometer(speedKmh: Float) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceTint,
        ),
    ) {
        Text(
            text = "${speedKmh.roundToInt()} km/h",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.padding(16.dp),
        )
    }
}
