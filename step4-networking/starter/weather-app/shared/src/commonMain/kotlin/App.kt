import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import network.CountryApi
import network.WeatherApi
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import weather.Weather

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

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Surface {
            var listCountries: List<Country> by remember { mutableStateOf(listOf()) }

            LaunchedEffect(Unit) {
                listCountries = CountryApi().getAllCountries().sortedBy { it.name.common }
            }

            LazyColumn() {
                items(items = listCountries) {
                    CountryCard(modifier = Modifier, country = it)
                }
            }
        }
    }
}

data class WeatherScreen(val cityName: String, val lat: Double, val long: Double) : Screen {
    @Composable
    override fun Content() {
        Surface {
            LazyColumn {
                item {
                    WeatherCard(
                        modifier = Modifier,
                        cityName = cityName,
                        lat = lat,
                        long = long
                    )
                }
                item {
                    WeatherCard(
                        modifier = Modifier,
                        cityName = "Your location",
                        lat = -25.7479, // This will eventually be your location
                        long = 28.2293
                    )
                }
            }
        }
    }
}

@Composable
fun CountryCard(modifier: Modifier, country: Country) {
    Card(
        modifier = modifier.fillMaxWidth().padding(4.dp),
        elevation = 10.dp,
        shape = MaterialTheme.shapes.medium
    ) {
        Row(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            Column(modifier = Modifier.width(130.dp).height(128.dp)) {
                Flag(modifier = Modifier.fillMaxWidth().padding(8.dp), country.flags)
            }
            Column(modifier = Modifier.fillMaxWidth().height(128.dp).padding(8.dp)) {
                CountryNames(name = country.name)
                if (country.capital.isNotEmpty() && country.capitalInfo != null) {
                    WeatherButton(capitals = country.capital, capitalInfo = country.capitalInfo)
                }
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
    Text(modifier = modifier, text = name.common, style = MaterialTheme.typography.body1)
    Text(modifier = modifier, text = name.official, style = MaterialTheme.typography.body2)
}

@Composable
fun WeatherButton(modifier: Modifier = Modifier, capitals: List<String>, capitalInfo: CapitalInfo) {
    val navigator = LocalNavigator.currentOrThrow
    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.Bottom
    ) {
        LazyColumn {
            items(capitals) {
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

    //TODO

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
                    "Feels like: ${weather.main.feels_like}",
                    style = MaterialTheme.typography.body1
                )
                Text("Temp: ${weather.main.temp}", style = MaterialTheme.typography.body1)
            }
        }
    }
}