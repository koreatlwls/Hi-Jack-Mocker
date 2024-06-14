package com.koreatlwls.hjm.extensions

import com.koreatlwls.hjm.model.ApiUiState
import com.koreatlwls.hjm.model.CustomUiState
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONObject

internal fun ResponseBody.extractResponseJson(): JSONObject {
    val jsonString = source().buffer.snapshot().utf8()
    return JSONObject(jsonString)
}

internal fun RequestBody.extractRequestJson(): JSONObject {
    val buffer = Buffer()
    writeTo(buffer)
    val jsonString = buffer.readUtf8()
    return JSONObject(jsonString)
}

internal fun Response.toCustomUiState(): CustomUiState = CustomUiState(
    apiUiState = toApiUiState(),
    requestUiState = CustomUiState.RequestUiState(
        headerKeys = this.request.headers.names().toImmutableList(),
        headerValues = this.request.headers.names().toList().map { header(it) ?: "" }
            .toImmutableList(),
        bodyItems = this.request.body?.extractRequestJson()?.parseJsonObjectToGroupedList()
            ?: persistentListOf()
    ),
    responseUiState = CustomUiState.ResponseUiState(
        bodyItems = this.body?.extractResponseJson()?.parseJsonObjectToGroupedList()
            ?: persistentListOf()
    )
)

internal fun Response.toApiUiState(): ApiUiState = ApiUiState(
    method = this.request.method,
    scheme = this.request.url.scheme,
    host = this.request.url.host,
    path = this.request.url.pathSegments.joinToString("/"),
    code = this.code,
    queryKeys = this.request.url.queryParameterNames.toImmutableList(),
    queryValues = this.request.url.queryParameterNames.toList()
        .map { this.request.url.queryParameter(it) ?: "" }
        .toImmutableList(),
)