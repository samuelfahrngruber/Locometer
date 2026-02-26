package at.locometer

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.core.content.ContextCompat
import at.locometer.business.LiveInfo
import at.locometer.dataaccess.AndroidLocationRepository
import at.locometer.dataaccess.OverpassApiRepository
import at.locometer.widgets.DebugWidget
import at.locometer.widgets.MaxSpeedWidget
import at.locometer.widgets.SpeedWidget
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

        val androidLocationRepository = AndroidLocationRepository(this, appScope)
        val overpassApiRepository = OverpassApiRepository()
        val liveInfo = LiveInfo(androidLocationRepository, overpassApiRepository)

        val permissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) {}

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        setContent {
            Column() {
                SpeedWidget(liveInfo)
                MaxSpeedWidget(liveInfo)
                DebugWidget(liveInfo)
            }
        }
    }
}
