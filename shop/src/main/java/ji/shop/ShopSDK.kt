package ji.shop

object ShopSDK {
    private var authenticationToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE3Mjg0NTUyMDIsImlhdCI6MTcyODQ1NTAyMn0.jADTGEdN5YN-hmNoDvGJAnycU6IY-OKg8V98s1PaLSk"
    private var accessToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE3NzM3NjA3MDQsImlhdCI6MTc3Mzc1OTgwNH0.bJEtjYfA46UEsV8sdsBACmGPuV4Jg-YaI7HoMJeERq8"
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