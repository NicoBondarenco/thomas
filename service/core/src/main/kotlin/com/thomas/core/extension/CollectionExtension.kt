package com.thomas.core.extension

fun <T> MutableList<T>.addIfAbsent(
    element: T
) = if (!this.any { it == element }) {
    this.add(element)
} else {
    false
}
