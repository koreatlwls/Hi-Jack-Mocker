package com.koreatlwls.acr.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class CustomUiState(
    val method: String = "",
    val scheme: String = "",
    val host: String = "",
    val path: String = "",
    val requestUiState: RequestUiState = RequestUiState(),
    val responseUiState: ResponseUiState = ResponseUiState(),
){
    val fullUrl = "$scheme://$host/$path${makeQueries()}"

    private fun makeQueries(): String {
        return requestUiState.queryKeys.zip(requestUiState.queryValues)
            .flatMap { (str1, str2) -> listOf("$str1=$str2") }
            .joinToString("&", prefix = "?")
    }

    data class RequestUiState(
        val queryKeys: ImmutableList<String> = persistentListOf(),
        val queryValues: ImmutableList<String> = persistentListOf(),
        val headerKeys: ImmutableList<String> = persistentListOf(),
        val headerValues: ImmutableList<String> = persistentListOf(),
        val bodyItems: ImmutableList<JsonItem> = persistentListOf(),
    )

    @JvmInline
    value class ResponseUiState(
        val bodyItems: ImmutableList<JsonItem> = persistentListOf(),
    )
}

