package location
interface LocationService {
    fun getLocation(): String?
}
expect fun getLocationService(): LocationService