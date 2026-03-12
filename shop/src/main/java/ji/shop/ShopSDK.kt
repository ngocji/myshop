package ji.shop

object ShopSDK {
    private var accessToken: String = ""

    fun init(accessToken: String) {
        this.accessToken = accessToken
    }

    fun getAccessToken() = accessToken
}