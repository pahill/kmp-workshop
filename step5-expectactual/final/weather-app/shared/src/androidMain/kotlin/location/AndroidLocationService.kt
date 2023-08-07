package location

import java.util.Locale

class AndroidLocationService() : LocationService {
    override fun getLocation(): String? {
        return Locale.getDefault().country
    }
}

actual fun getLocationService(): LocationService = AndroidLocationService()