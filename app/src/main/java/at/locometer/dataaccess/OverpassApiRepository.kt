package at.locometer.dataaccess

import android.location.Location
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

class OverpassApiRepository {

    private val RADIUS_M = 10_000

    private val service: OverpassService by lazy {
        Retrofit.Builder()
            .baseUrl("https://overpass-api.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OverpassService::class.java)
    }

    private var cachedTile: OverpassResponse? = null
    private var cachedTileCenter: Location? = null

    suspend fun queryTrainMaxSpeedKmh(at: Location): Int? = withContext(Dispatchers.IO) {
        val response = fetchOverpassData(at) ?: return@withContext null

        var nearestSpeed: Int? = null
        var minDistance = Float.MAX_VALUE

        for (element in response.elements) {
            val speedStr = element.tags?.maxspeed ?: continue
            val speed = speedStr.filter { it.isDigit() }.toIntOrNull() ?: continue
            
            val center = element.center ?: continue
            val results = FloatArray(1)
            Location.distanceBetween(at.latitude, at.longitude, center.lat, center.lon, results)
            val distance = results[0]

            if (distance < minDistance) {
                minDistance = distance
                nearestSpeed = speed
            }
        }

        nearestSpeed
    }

    private suspend fun fetchOverpassData(location: Location): OverpassResponse? {
        cachedTileCenter?.let {
            if (it.distanceTo(location) < RADIUS_M) {
                return cachedTile
            }
        }

        val query = """
            [out:json][timeout:25];
            (
              way["railway"~"rail|light_rail|subway|tram"](around:$RADIUS_M, ${location.latitude}, ${location.longitude});
            );
            out center;
        """.trimIndent()

        apiCalls.value++
        cachedTile = service.query(query)
        cachedTileCenter = location
        return cachedTile
    }

    val apiCalls = MutableStateFlow(0)

    interface OverpassService {
        @GET("interpreter")
        suspend fun query(@Query("data") data: String): OverpassResponse
    }

    data class OverpassResponse(
        @SerializedName("elements") val elements: List<Element>
    )

    data class Element(
        @SerializedName("tags") val tags: Tags?,
        @SerializedName("center") val center: Center?
    )

    data class Center(
        @SerializedName("lat") val lat: Double,
        @SerializedName("lon") val lon: Double
    )

    data class Tags(
        @SerializedName("maxspeed") val maxspeed: String?
    )
}
