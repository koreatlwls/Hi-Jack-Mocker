package com.koreatlwls.hjm.extensions

fun String.isParentKey(): Boolean {
    val regex = """\[\d+]$""".toRegex()
    return regex.containsMatchIn(this).not()
}