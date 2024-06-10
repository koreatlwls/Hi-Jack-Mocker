package com.koreatlwls.acr.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.acr.extensions.parseGroupedListToJSONObject
import com.koreatlwls.acr.extensions.toUiState
import com.koreatlwls.acr.model.AcrActions
import com.koreatlwls.acr.model.AcrUiState
import com.koreatlwls.acr.model.JsonItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
internal class AcrViewModel @Inject constructor(
    @Named("send") private val sendChannel: Channel<Response>,
    @Named("receive") private val receiveChannel: Channel<Response>,
) : ViewModel() {

    private val response = mutableStateOf<Response?>(null)
    val acrUiState = mutableStateOf(AcrUiState())
    val onFinishEvent = mutableStateOf(false)

    init {
        viewModelScope.launch {
            response.value = sendChannel.receive()
            acrUiState.value = response.value?.toUiState() ?: AcrUiState()
        }
    }

    fun handleActions(action: AcrActions.Updates) {
        when (action) {
            AcrActions.Updates.NewRequest -> sendNewRequest()
            AcrActions.Updates.NewResponse -> sendNewResponse()
            is AcrActions.Updates.RequestHeaderValue -> updateRequestHeaderValue(
                index = action.index,
                value = action.value
            )

            is AcrActions.Updates.RequestQueryValue -> updateRequestQueryValue(
                index = action.index,
                value = action.value
            )

            is AcrActions.Updates.RequestBodyValue -> updateRequestBodyValue(
                bodyItems = acrUiState.value.requestUiState.bodyItems,
                key = action.key,
                newValue = action.newValue
            )

            is AcrActions.Updates.ResponseBodyValue -> updateResponseBodyValue(
                bodyItems = acrUiState.value.responseUiState.bodyItems,
                key = action.key,
                newValue = action.newValue
            )
        }
    }

    private fun updateRequestQueryValue(index: Int, value: String) {
        val queryValues = acrUiState.value.requestUiState.queryValues
            .toPersistentList()
            .add(index, value)
        acrUiState.value = acrUiState.value.copy(
            requestUiState = acrUiState.value.requestUiState.copy(queryValues = queryValues)
        )
    }

    private fun updateRequestHeaderValue(index: Int, value: String) {
        val headerValues = acrUiState.value.requestUiState.headerValues
            .toPersistentList()
            .add(index, value)
        acrUiState.value = acrUiState.value.copy(
            requestUiState = acrUiState.value.requestUiState.copy(headerValues = headerValues)
        )
    }

    private fun updateRequestBodyValue(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: String
    ) {
        val updateBodyItems = updateBodyItems(bodyItems, key, newValue)

        acrUiState.value = acrUiState.value.copy(
            requestUiState = acrUiState.value.requestUiState.copy(bodyItems = updateBodyItems)
        )
    }

    private fun updateResponseBodyValue(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: String
    ) {
        val updateBodyItems = updateBodyItems(bodyItems, key, newValue)

        acrUiState.value = acrUiState.value.copy(
            responseUiState = AcrUiState.ResponseUiState(bodyItems = updateBodyItems)
        )
    }

    private fun updateBodyItems(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: String
    ): ImmutableList<JsonItem> {
        val updateBodyItems = bodyItems.map { bodyItem ->
            when (bodyItem) {
                is JsonItem.SingleItem -> {
                    if (bodyItem.key == key) {
                        bodyItem.copy(value = newValue)
                    } else {
                        bodyItem
                    }
                }

                is JsonItem.ObjectGroup -> {
                    bodyItem.copy(items = updateBodyItems(bodyItem.items, key, newValue))
                }

                is JsonItem.ArrayGroup -> {
                    bodyItem.copy(items = updateBodyItems(bodyItem.items, key, newValue))
                }
            }
        }
        return updateBodyItems.toPersistentList()
    }

    private fun sendNewRequest() {
        response.value?.let { safeResponse ->
            viewModelScope.launch(Dispatchers.IO) {
                val request = safeResponse
                    .request
                    .newBuilder()
                    .also { builder ->
                        builder.url(acrUiState.value.fullUrl)
                        acrUiState.value
                            .requestUiState
                            .headerKeys
                            .forEachIndexed { index, key ->
                                builder.addHeader(
                                    key,
                                    acrUiState.value.requestUiState.headerValues[index]
                                )
                            }

                        acrUiState.value
                            .requestUiState
                            .bodyItems
                            .parseGroupedListToJSONObject()
                            ?.let { safeJson ->
                                builder.put(safeJson.toString().toRequestBody())
                            }
                    }.build()

                try {
                    val response = OkHttpClient().newCall(request).execute()
                    receiveChannel.send(response)
                    onFinishEvent.value = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun sendNewResponse() {
        response.value?.let { safeResponse ->
            acrUiState.value
                .responseUiState
                .bodyItems
                .parseGroupedListToJSONObject()?.let { safeJson ->
                    val updateResponse = safeResponse
                        .newBuilder()
                        .body(safeJson.toString().toResponseBody())
                        .build()

                    receiveChannel.trySend(updateResponse)
                    onFinishEvent.value = true
                }
        }
    }
}