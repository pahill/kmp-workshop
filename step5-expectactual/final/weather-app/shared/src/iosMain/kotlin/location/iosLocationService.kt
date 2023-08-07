package location

import platform.Foundation.NSLocale
import platform.Foundation.NSLocaleCountryCode
import platform.Foundation.currentLocale

class iOSLocationService() : LocationService {
    override fun getLocation(): String? {
        return NSLocale.currentLocale().objectForKey(NSLocaleCountryCode).toString()
    }
}

actual fun getLocationService(): LocationService = iOSLocationService()