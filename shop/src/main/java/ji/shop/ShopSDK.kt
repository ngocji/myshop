package ji.shop

object ShopSDK {
    private var authenticationToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE3Mjg0NTUyMDIsImlhdCI6MTcyODQ1NTAyMn0.jADTGEdN5YN-hmNoDvGJAnycU6IY-OKg8V98s1PaLSk"
    private var accessToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE3NzM2NzU4OTgsImlhdCI6MTc3MzY3NDk5OH0.TpbGUHkiDttl8FQb6fVBUdwKhgP_7Z3EVDok7AOA2O0"
    private var refreshToken: String = "F6LAontbgGwbCbgAZiM5BA"

    fun init(
        authenticationToken: String,
        accessToken: String,
        refreshToken: String
    ) {
        this.authenticationToken = authenticationToken
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun getAuthenticationToken() = authenticationToken
    fun getAccessToken() = accessToken
    fun getRefreshToken() = refreshToken
}