package com.thomas.management.data.extension

import com.thomas.core.extension.LETTERS_ONLY_REGEX_VALUE
import com.thomas.core.extension.onlyNumbers

internal val PERSON_NAME_REGEX = "[$LETTERS_ONLY_REGEX_VALUE\\- ]+".toRegex()
internal val EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
internal val GROUP_NAME_REGEX = "[${LETTERS_ONLY_REGEX_VALUE}0-9\\-._ ]+".toRegex()

//region VALIDATION DOCUMENT NUMBER
@Suppress("MagicNumber")
internal fun String.isValidDocumentNumber(): Boolean = this.onlyNumbers().let {
    it.length == 11 &&
        it.toCharArray().distinct().size != 1 &&
        it.hasValidVerificationDigits()
}

@Suppress("MagicNumber")
private fun String.hasValidVerificationDigits(): Boolean {
    val firstNineDigits = substring(0..8)
    val digits = substring(9..10)

    return firstNineDigits.calculateDigits() == digits
}

private fun String.calculateDigits(): String {
    val numbers = map { it.toString().toInt() }
    val firstDigit = numbers.digits()
    val secondDigit = (numbers + firstDigit).digits()

    return "$firstDigit$secondDigit"
}

@Suppress("MagicNumber")
private fun List<Int>.digits(): Int {
    val firstNineDigits = this
    val weights = ((this.size + 1) downTo 2).toList()
    val sum = firstNineDigits.withIndex().sumOf { (index, element) -> weights[index] * element }

    val remainder = sum % 11
    return if (remainder < 2) 0 else 11 - remainder
}

//endregion VALIDATION DOCUMENT NUMBER
