package com.thomas.core.extension

fun Int.isBetween(min: Int, max: Int): Boolean = this in min..max

fun Long.isBetween(min: Long, max: Long): Boolean = this in min..max

fun Long.toPositive() = if (this < 0) this * -1 else this