package at.locometer

import android.Manifest
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt

@Composable
@RequiresPermission(allOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION])
fun SpeedometerScreen(
    androidLocationAccess: AndroidLocationAccess
) {
    val speed by androidLocationAccess.speedKmh().collectAsState(0f)

    Speedometer(speed)
}

@Composable
fun Speedometer(speedKmh: Float) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "${speedKmh.roundToInt()} km/h",
            style = MaterialTheme.typography.displayLarge
        )
    }
}
