package ji.shop

object ShopSDK {
    private var authenticationToken: String = ""
    private var accessToken: String = ""
    private var refreshToken: String = "F6LAontbgGwbCbgAZiM5BA"
    private var venueId: String = "90"

    fun initToken(
        authenticationToken: String,
        accessToken: String,
        refreshToken: String,
    ) {
        this.authenticationToken = authenticationToken
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun initData(venueId: String) {
        this.venueId = venueId
    }

    fun getAuthenticationToken() = authenticationToken
    fun getAccessToken() = accessToken
    fun getRefreshToken() = refreshToken
    fun getVenueId() = venueId

    fun getEmail(): String {
        return "paulv@showslinger.com"
    }

    fun getPassword(): String {
        return "12345"
    }
}