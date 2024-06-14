package com.koreatlwls.hjm.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class ApiUiState(
    val method: String = "",
    val scheme: String = "",
    val host: String = "",
    val path: String = "",
    val code: Int = 0,
    val queryKeys: ImmutableList<String> = persistentListOf(),
    val queryValues: ImmutableList<String> = persistentListOf(),
) {
    val pathWithQueries = "$path${makeQueries()}"
    val fullUrl = "${scheme}://${host}/${path}${makeQueries()}"

    private fun makeQueries(): String {
        return queryKeys.zip(queryValues)
            .flatMap { (str1, str2) -> listOf("$str1=$str2") }
            .joinToString("&", prefix = "?")
    }
}
