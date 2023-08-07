package location
interface CountryCodeService {
    fun getCountryCode(): String?
}
expect fun getCountryCode(): CountryCodeService