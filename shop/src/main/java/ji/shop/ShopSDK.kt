package ji.shop

object ShopSDK {
    private var authenticationToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE3Mjg0NTUyMDIsImlhdCI6MTcyODQ1NTAyMn0.jADTGEdN5YN-hmNoDvGJAnycU6IY-OKg8V98s1PaLSk"
    private var accessToken: String =
        "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VyX2lkIjo0LCJleHAiOjE3NzM0MDk4NTYsImlhdCI6MTc3MzQwODk1Nn0.uoV4wsaH5z4OavcyNcpRiyVD4-64rcb1p5aKiDCFy68"
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