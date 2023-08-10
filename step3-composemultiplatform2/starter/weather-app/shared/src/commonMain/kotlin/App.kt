import androidx.compose.foundation.Image
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
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberImagePainter
import country.CapitalInfo
import country.Country
import country.Flags
import country.Name
import country.countries
import weather.getWeather

@Composable
fun App() {
    MaterialTheme {
        //TODO
    }
}

class HomeScreen : Screen {
    @Composable
    override fun Content() {
        Surface {
            LazyColumn() {
                items(items = countries) {
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
                        lat = 0.0, // Later this will be your actual location
                        long = 0.0
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
                if (country.capital.isNotEmpty() && country.capitalInfo != null){
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
                    //TODO
                }) {
                    Text(text = "$it weather")
                }
            }
        }
    }
}

@Composable
fun WeatherCard(modifier: Modifier, cityName: String, lat: Double, long: Double) {
    //TODO
}