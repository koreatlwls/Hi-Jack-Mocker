package com.koreatlwls.acr.model

internal data class ApiUiState(
    val method: String = "",
    val scheme: String = "",
    val host: String = "",
    val path: String = "",
    val code : Int = 0,
    val isSuccessful : Boolean = false,
)
