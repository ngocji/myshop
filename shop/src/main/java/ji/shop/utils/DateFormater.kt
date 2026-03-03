package ji.shop.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateFormater {
    fun format(time: Long, format: String): String {
        return try {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            dateFormat.format(Date(time))
        } catch (e: Exception) {
            ""
        }
    }
}