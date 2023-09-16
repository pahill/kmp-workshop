import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.CurrentScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberImagePainter
import country.CapitalInfo
import country.Country
import country.Flags
import country.Name
import location.getCountryCode
import network.CountryApi
import network.WeatherApi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import weather.Weather
import weather.celsiusToFahrenheit

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        Navigator(HomeScreen()) { navigator ->
            Scaffold(topBar = {
                if (navigator.canPop)
                    Button(modifier = Modifier.padding(4.dp).width(48.dp).height(48.dp), onClick = {
                        navigator.pop()
                    }) {
                        val painterResource = painterResource("back.xml")
                        Image(
                            painter = painterResource,
                            contentDescription = "Back"
                        )
                    }
            }) {
                CurrentScreen()
            }
        }
    }
}

class HomeScreen() : Screen {
    @Composable
    override fun Content() {
        Surface {
            var listCountries: MutableList<Country> by remember { mutableStateOf(mutableListOf()) }

            val countryCode = getCountryCode().getCountryCode()

            LaunchedEffect(Unit) {
                val tempCountries =
                    CountryApi().getAllCountries().sortedBy { it.name.common }.toMutableList()
                val currentCountry = tempCountries.first { it.cca2 == countryCode }
                tempCountries.remove(currentCountry)
                tempCountries.add(0, currentCountry)
                listCountries = tempCountries
            }

            LazyColumn() {
                items(items = listCountries) {
                    CountryCard(
                        modifier = Modifier,
                        country = it,
                        currentCountry = it.cca2 == countryCode
                    )
                }
            }
        }
    }
}

data class WeatherScreen(val cityName: String, val lat: Double, val long: Double) : Screen {
    @Composable
    override fun Content() {
        Surface {
            WeatherCard(
                modifier = Modifier,
                cityName = cityName,
                lat = lat,
                long = long
            )
        }
    }
}

@Composable
fun CountryCard(modifier: Modifier, country: Country, currentCountry: Boolean) {
    Card(
        modifier = modifier.fillMaxWidth().padding(4.dp),
        elevation = 10.dp,
        shape = MaterialTheme.shapes.medium,
    ) {
        if (currentCountry) {
            Box(modifier = Modifier.border(5.dp, Color.DarkGray)) {
                Country(Modifier, country)
            }
        } else {
            Country(Modifier, country)
        }
    }
}

@Composable
fun Country(modifier: Modifier, country: Country) {
    Row(modifier = Modifier.padding(8.dp)) {
        Column(modifier = Modifier.width(130.dp)) {
            Flag(modifier = Modifier.fillMaxWidth().padding(8.dp), country.flags)
        }
        Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
            CountryNames(name = country.name)
            if (country.capital.isNotEmpty() && country.capitalInfo != null) {
                WeatherButton(capitals = country.capital, capitalInfo = country.capitalInfo)
            }
        }
    }
}

@Composable
fun Flag(modifier: Modifier = Modifier, flag: Flags) {
    Card(
        modifier = modifier,
        elevation = 10.dp,
        shape = MaterialTheme.shapes.small
    ) {
        val painterResource = rememberImagePainter(flag.png)
        Image(
            painter = painterResource,
            contentDescription = flag.alt,
            contentScale = ContentScale.FillBounds
        )
    }
}

@Composable
fun CountryNames(modifier: Modifier = Modifier, name: Name) {
    Column(modifier = modifier) {
        Text(text = name.common, style = MaterialTheme.typography.body1)
        Text(text = name.official, style = MaterialTheme.typography.body2)
    }
}

@Composable
fun WeatherButton(modifier: Modifier = Modifier, capitals: List<String>, capitalInfo: CapitalInfo) {
    val navigator = LocalNavigator.currentOrThrow
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Bottom
    ) {
        Column {
            capitals.forEach {
                Button(onClick = {
                    navigator.push(
                        WeatherScreen(
                            cityName = it,
                            lat = capitalInfo.latlng[0],
                            long = capitalInfo.latlng[1]
                        )
                    )
                }) {
                    Text(text = "$it weather")
                }
            }
        }
    }
}

@Composable
fun WeatherCard(modifier: Modifier, cityName: String, lat: Double, long: Double) {
    var weather: Weather? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        weather = WeatherApi().getWeather(lat, long)
    }

    weather?.let { weather ->
        Card(
            modifier = modifier.fillMaxWidth().padding(4.dp),
            elevation = 10.dp,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    modifier = Modifier.align(CenterHorizontally),
                    text = cityName,
                    style = MaterialTheme.typography.h4
                )
                val painterResource =
                    rememberImagePainter("https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png")
                Image(
                    modifier = Modifier.width(128.dp).height(128.dp).align(CenterHorizontally),
                    painter = painterResource,
                    contentDescription = weather.weather[0].description,
                    contentScale = ContentScale.Fit
                )
                Text(
                    "Feels like: ${weather.main.feels_like} 'C / ${celsiusToFahrenheit(weather.main.feels_like)} 'F",
                    style = MaterialTheme.typography.body1
                )
                Text("Temp: ${weather.main.temp} 'C / ${celsiusToFahrenheit(weather.main.temp)} 'F", style = MaterialTheme.typography.body1)
            }
        }
    }
}