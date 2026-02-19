package ji.shop.utils

import ji.shop.utils.NumberFormater.formatNumberLocale
import java.math.BigDecimal
import java.text.DecimalFormatSymbols
import java.text.NumberFormat
import java.util.Locale

object NumberFormater {
    private const val MAX_FORMAT_RESULT_VALUE_TOOL = 25
    const val MAX_FORMAT_RESULT_HOME = 30

    fun formatNumberLocale(
        value: Number?,
        prefix: String = "$",
        maxLength: Int = MAX_FORMAT_RESULT_VALUE_TOOL,
        locale: Locale = Locale.getDefault(),
        maxDecimal: Int = 3
    ): String {
        val number = when (value) {
            is BigDecimal -> value
            is Double -> {
                if (value.isInfinite()) {
                    return "∞"
                }

                if (value.isNaN()) {
                    return "NaN"
                }
                BigDecimal(value.toString())
            }

            is Float -> {
                if (value.isInfinite()) {
                    return "∞"
                }

                if (value.isNaN()) {
                    return "NaN"
                }

                BigDecimal(value.toString())
            }

            else -> BigDecimal(value?.toLong() ?: return "")
        }

        val numberAsDouble = number.toDouble()
        Log.d("numberAsDouble $numberAsDouble")


        val nf = NumberFormat.getNumberInstance(locale).apply {
            maximumFractionDigits = maxDecimal
            minimumFractionDigits = 0
        }
        val formatted = nf.format(number)
        return prefix + (if (number.toPlainString().length > maxLength) {
            val exp = number.stripTrailingZeros().toEngineeringString()
            if (exp.length > maxLength) {
                String.format(locale, "%.3E", number.toPlainString().toDouble())
            } else exp
        } else {
            formatted
        })
    }

    fun firstNonZeroDecimalPosition(value: Number): Int {
        val text = when (value) {
            is BigDecimal -> value.stripTrailingZeros().toPlainString()
            is Double -> value.toBigDecimal().stripTrailingZeros().toPlainString()
            is Float -> value.toBigDecimal().stripTrailingZeros().toPlainString()
            else -> return 0
        }
        val index = text.indexOf('.')
        if (index < 0) return 0

        val decimals = text.substring(index + 1)
        for ((i, c) in decimals.withIndex()) {
            if (c != '0') return i + 1
        }
        return 0
    }

    fun getGroupSeparator(locale: Locale = Locale.getDefault()): Char {
        val symbols = DecimalFormatSymbols.getInstance(locale)
        return symbols.groupingSeparator
    }

    fun parseFormatedNumberToOriginalString(
        text: String,
        locale: Locale = Locale.getDefault()
    ): BigDecimal? {
        val trimmed = text.trim()

        if (trimmed.isEmpty()) return null
        val groupingSeparator = getGroupSeparator(locale)

        val cleaned = text.replace(groupingSeparator.toString(), "")
        return BigDecimal(cleaned)
    }

    fun unformatNumberToPlainString(
        text: String,
        locale: Locale = Locale.getDefault()
    ): String {
        val trimmed = text.trim()

        if (trimmed.isEmpty()) return text
        val groupingSeparator = getGroupSeparator(locale)
        if (trimmed.contains(groupingSeparator)) {
            val cleaned = text.replace(groupingSeparator.toString(), "")
            return cleaned
        } else {
            return trimmed
        }
    }

    fun unformatNumberFromOriginalString(
        text: String,
        locale: Locale = Locale.getDefault()
    ): String {
        return parseFormatedNumberToOriginalString(text, locale)
            ?.toPlainString()
            .orEmpty()
    }

    fun Number.toPlanString(): String {
        return when (this) {
            is BigDecimal -> this.toPlainString()
            else -> this.toString()
        }
    }

    fun Number.isNaNOrInfinity(): Boolean {
        return when (this) {
            is Double -> this.isInfinite() || this.isNaN()
            is Float -> this.isInfinite() || this.isNaN()
            else -> false
        }
    }
}