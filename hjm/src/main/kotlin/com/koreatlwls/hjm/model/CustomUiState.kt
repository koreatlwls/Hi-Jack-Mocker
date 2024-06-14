package com.koreatlwls.hjm.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class CustomUiState(
    val apiUiState: ApiUiState = ApiUiState(),
    val requestUiState: RequestUiState = RequestUiState(),
    val responseUiState: ResponseUiState = ResponseUiState(),
) {
    data class RequestUiState(
        val headerKeys: ImmutableList<String> = persistentListOf(),
        val headerValues: ImmutableList<String> = persistentListOf(),
        val bodyItems: ImmutableList<JsonItem> = persistentListOf(),
    )

    @JvmInline
    value class ResponseUiState(
        val bodyItems: ImmutableList<JsonItem> = persistentListOf(),
    )
}

