package ji.shop.data.domain

enum class Status(val key: String) {
    COMPLETE("Completed"),
    PAID("Paid"),
    IN_PROGRESS("InProgress"),
    REFUND("Refund"),
    CANCELLED("Cancelled");

    companion object {
        fun safe(name: String?): Status {
            return name?.lowercase()?.let { name ->
                entries.find { it.key.lowercase() == name }
            } ?: IN_PROGRESS
        }
    }
}