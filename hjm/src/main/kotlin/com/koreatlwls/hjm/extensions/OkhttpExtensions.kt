package com.koreatlwls.hjm.extensions

import com.koreatlwls.hjm.model.ApiUiState
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.model.JsonItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import okhttp3.RequestBody
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONException
import org.json.JSONObject

internal fun ResponseBody.extractResponseJson(): JSONObject? {
    return try {
        val jsonString = source().buffer.snapshot().utf8()
        JSONObject(jsonString)
    } catch (e: JSONException) {
        e.printStackTrace()
        null
    }
}

internal fun RequestBody.extractRequestJson(): JSONObject {
    val buffer = Buffer()
    writeTo(buffer)
    val jsonString = buffer.readUtf8()
    return JSONObject(jsonString)
}

internal fun Response.toCustomUiState(): CustomUiState {
    val headers = this.request.headers
    val headerKeys = (0 until headers.size).map { headers.name(it) }.toImmutableList()
    val headerValues = (0 until headers.size).map { headers.value(it) }.toImmutableList()

    return CustomUiState(
        apiUiState = toApiUiState(),
        requestUiState = CustomUiState.RequestUiState(
            headerKeys = headerKeys,
            headerValues = headerValues,
            bodyItems = this.request.body
                ?.extractRequestJson()
                ?.parseJsonObjectToGroupedList()
                ?.sortedJsonItem()
                ?: persistentListOf()
        ),
        responseUiState = CustomUiState.ResponseUiState(
            bodyItems = this.body
                ?.extractResponseJson()
                ?.parseJsonObjectToGroupedList()
                ?.sortedJsonItem()
                ?: persistentListOf()
        )
    )
}

private fun ImmutableList<JsonItem>.sortedJsonItem(): ImmutableList<JsonItem> {
    return this.sortedWith(
        compareBy { item ->
            when (item) {
                is JsonItem.ArrayGroup -> 0
                is JsonItem.ObjectGroup -> 1
                is JsonItem.SingleItem -> 2
            }
        }
    ).toImmutableList()
}

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