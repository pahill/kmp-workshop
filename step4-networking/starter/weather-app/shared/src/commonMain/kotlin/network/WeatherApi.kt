package network

import com.myapplication.Config
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import weather.Weather

class WeatherApi {
    private val httpClient = HttpClient {
        //TODO Setup the HTTP client and install the plugins
    }

    suspend fun getWeather(lat: Double, long: Double): Weather {
        TODO() // Call the webservice and get the body
    }
}