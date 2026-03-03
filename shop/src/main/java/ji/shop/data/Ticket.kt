package ji.shop.data

data class Ticket(
    val image: Any?,
    val name: String?,
    val date: Long = System.currentTimeMillis(),
    val ticketDayPass: String?,
    val info: Map<String, Double> = mapOf()
)
