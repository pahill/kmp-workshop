package cache

import country.Country
import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import network.CountryApi

class CountrySDK {
    private val cache: KStore<List<Country>> = TODO()
    private val api: CountryApi = CountryApi()

    @Throws(Exception::class)
    suspend fun getLaunches(): List<Country> {
        return TODO()
    }
}

expect fun pathToCountryCache(): String