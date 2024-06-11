package com.koreatlwls.acr.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class CustomUiState(
    val apiUiState: ApiUiState = ApiUiState(),
    val requestUiState: RequestUiState = RequestUiState(),
    val responseUiState: ResponseUiState = ResponseUiState(),
) {
    val fullUrl = "${apiUiState.scheme}://${apiUiState.host}/${apiUiState.path}${makeQueries()}"

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

