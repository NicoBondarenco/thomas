package com.thomas.core.extension

import java.text.Normalizer
import java.util.UUID

const val ACCENTS_LOWER_REGEX_VALUE = "aàáâãäạảăắặằẳẵấậầẩẫåāącçčćĉċdđðďeèéêëẹẻẽếệềểễēĕėęěgĝğġģhĥħiìíîïịỉĩīĭį̇ıjĵkķĸlĺļľŀłnñńņňŉŋoòóôõöọỏốộồổỗơớợờởỡøōŏőŕŗřsšśŝştţťŧuùúûüụủũưứựừửữŭūůűųwŵyýÿỵỳỷỹŷzžźż"
const val ACCENTS_UPPER_REGEX_VALUE = "AÀÁÂÃÄẠẢĂẮẶẰẲẴẤẬẦẨẪÅĀĄCÇČĆĈĊDĐÐĎEÈÉÊËẸẺẼẾỆỀỂỄĒĔĖĘĚGĜĞĠĢHĤĦIÌÍÎÏỊỈĨĪĬĮ̇IJĴKĶĸLĹĻĽĿŁNÑŃŅŇNŊOÒÓÔÕÖỌỎỐỘỒỔỖƠỚỢỜỞỠØŌŎŐŔŖŘSŠŚŜŞTŢŤŦUÙÚÛÜỤỦŨƯỨỰỪỬỮŬŪŮŰŲWŴYÝŸỴỲỶỸŶZŽŹŻ"
const val LETTERS_REGEX_VALUE = "A-Za-z \\-$ACCENTS_LOWER_REGEX_VALUE$ACCENTS_UPPER_REGEX_VALUE"
const val LETTERS_ONLY_REGEX_VALUE = "A-Za-z$ACCENTS_LOWER_REGEX_VALUE$ACCENTS_UPPER_REGEX_VALUE"

val UUID_REGEX = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}\$".toRegex()

val EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$".toRegex()
val PERSON_NAME_REGEX = "[$LETTERS_REGEX_VALUE]+".toRegex()
val GROUP_NAME_REGEX = "[${LETTERS_REGEX_VALUE}0-9]+".toRegex()

fun String.onlyNumbers() = this.replace("[^0-9]".toRegex(), "")

fun String.onlyLettersAndNumbers() = this.filter { it.isLetterOrDigit() }

fun String.toUUIDOrNull(): UUID? = this.takeIf { it.matches(UUID_REGEX) }?.let { UUID.fromString(it) }

fun String.repeat(times: Int) =
    StringBuilder().let { sb ->
        repeat((1..times).count()) { sb.append(this) }
        sb.toString()
    }

fun String.isQuoted() = (this.startsWith("\"") && this.endsWith("\""))

fun String.unquote() = if (this.isQuoted()) {
    this.substring(1, this.length - 1)
} else {
    this
}

fun String.unaccented() = Normalizer.normalize(this, Normalizer.Form.NFD)
    .replace("[\\p{InCombiningDiacriticalMarks}]".toRegex(), "")

fun String.unaccentedLower() = this.unaccented()
    .lowercase()

fun String.isValidDocumentNumber(): Boolean = this.let { value ->
    value.takeIfValidDocumentLength(11)?.toIntList()?.let {
        it.add(it.sumDoc((10 downTo 2).toList()).mod11DocumentNumber())
        it.add(it.sumDoc((11 downTo 2).toList()).mod11DocumentNumber())
        it.joinToString("")
    } == this.onlyNumbers()
}

fun String.isValidRegistrationNumber(): Boolean = this.let { value ->
    value.takeIfValidDocumentLength(14)?.toIntList()?.let {
        val counter = ((5 downTo 2) + (9 downTo 2)).toList()
        it.add(it.sumDoc(counter).mod11RegistrationNumber())
        it.add(it.sumDoc(listOf(6) + counter).mod11RegistrationNumber())
        it.joinToString("")
    } == this.onlyNumbers()
}

private fun String.takeIfValidDocumentLength(length: Int): String? = this.onlyNumbers()
    .takeIf {
        it.length == length && it.toCharArray().distinct().size != 1
    }?.substring(0, length - 2)

private fun String.toIntList() = this.map { it.digitToInt() }.toMutableList()

private fun Int.mod11DocumentNumber() = (11.minus(this.mod(11))
    .takeIf { it < 10 } ?: 0)

private fun Int.mod11RegistrationNumber() = this.mod(11)
    .takeIf { it > 1 }?.let { 11.minus(it) } ?: 0

private fun List<Int>.sumDoc(counter: List<Int>) = counter
    .mapIndexed { index, value -> value.times(this[index]) }.sum()

