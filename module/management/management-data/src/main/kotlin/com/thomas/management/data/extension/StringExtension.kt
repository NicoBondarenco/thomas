package com.thomas.management.data.extension

import com.thomas.core.extension.LETTERS_ONLY_REGEX_VALUE
import com.thomas.core.extension.onlyNumbers

internal val NATURAL_NAME_REGEX = "[$LETTERS_ONLY_REGEX_VALUE\\- ]+".toRegex()
internal val LEGAL_NAME_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-. ]+".toRegex()

@Suppress("MagicNumber")
internal fun String.isValidDocumentNumber(): Boolean = this.isValidDocument(
    length = 11,
    weights = (10 downTo 2).toList(),
)

@Suppress("MagicNumber")
internal fun String.isValidRegistrationNumber(): Boolean = this.isValidDocument(
    length = 14,
    weights = (5 downTo 2).toList() + (9 downTo 2).toList(),
)

private fun String.isValidDocument(
    length: Int,
    weights: List<Int>,
) = this.onlyNumbers().let {
    it.length == this.length && it.hasValidLength(length) && it.hasValidVerificationDigits(weights)
}

private fun String.hasValidLength(
    length: Int,
) = this.length == length && this.toCharArray().distinct().size != 1

@Suppress("MagicNumber")
private fun String.hasValidVerificationDigits(
    weights: List<Int>,
): Boolean {
    val firstDigits = this.substring(0..this.length - 3)
    val digits = substring(this.length - 2..<this.length)

    return firstDigits.calculateDigits(weights) == digits
}

private fun String.calculateDigits(
    weights: List<Int>,
): String {
    val numbers = map { it.toString().toInt() }
    val firstDigit = numbers.digits(weights)
    val secondDigit = (numbers + firstDigit).digits(listOf(weights[0] + 1) + weights)

    return "$firstDigit$secondDigit"
}

private fun List<Int>.digits(
    weights: List<Int>,
): Int = this.withIndex().sumOf { (index, element) ->
    weights[index] * element
}.let {
    it % 11
}.takeIf {
    it > 1
}?.let {
    11 - it
} ?: 0
