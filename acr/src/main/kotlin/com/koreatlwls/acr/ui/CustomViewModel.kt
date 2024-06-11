package com.koreatlwls.acr.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.acr.extensions.parseGroupedListToJSONObject
import com.koreatlwls.acr.extensions.toUiState
import com.koreatlwls.acr.model.CustomActions
import com.koreatlwls.acr.model.CustomUiState
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
internal class CustomViewModel @Inject constructor(
    @Named("send") private val sendChannel: Channel<Response>,
    @Named("receive") private val receiveChannel: Channel<Response>,
) : ViewModel() {

    private val response = mutableStateOf<Response?>(null)
    val customUiState = mutableStateOf(CustomUiState())
    val onFinishEvent = mutableStateOf(false)

    init {
        viewModelScope.launch {
            response.value = sendChannel.receive()
            customUiState.value = response.value?.toUiState() ?: CustomUiState()
        }
    }

    fun handleActions(action: CustomActions.Updates) {
        when (action) {
            CustomActions.Updates.NewRequest -> sendNewRequest()
            CustomActions.Updates.NewResponse -> sendNewResponse()
            is CustomActions.Updates.RequestHeaderValue -> updateRequestHeaderValue(
                index = action.index,
                value = action.value
            )

            is CustomActions.Updates.RequestQueryValue -> updateRequestQueryValue(
                index = action.index,
                value = action.value
            )

            is CustomActions.Updates.RequestBodyValue -> updateRequestBodyValue(
                bodyItems = customUiState.value.requestUiState.bodyItems,
                key = action.key,
                newValue = action.newValue
            )

            is CustomActions.Updates.ResponseBodyValue -> updateResponseBodyValue(
                bodyItems = customUiState.value.responseUiState.bodyItems,
                key = action.key,
                newValue = action.newValue
            )
        }
    }

    private fun updateRequestQueryValue(index: Int, value: String) {
        val queryValues = customUiState.value.requestUiState.queryValues
            .toPersistentList()
            .removeAt(index)
            .add(index, value)
        customUiState.value = customUiState.value.copy(
            requestUiState = customUiState.value.requestUiState.copy(queryValues = queryValues)
        )
    }

    private fun updateRequestHeaderValue(index: Int, value: String) {
        val headerValues = customUiState.value.requestUiState.headerValues
            .toPersistentList()
            .removeAt(index)
            .add(index, value)
        customUiState.value = customUiState.value.copy(
            requestUiState = customUiState.value.requestUiState.copy(headerValues = headerValues)
        )
    }

    private fun updateRequestBodyValue(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: String
    ) {
        val updateBodyItems = updateBodyItems(bodyItems, key, newValue)

        customUiState.value = customUiState.value.copy(
            requestUiState = customUiState.value.requestUiState.copy(bodyItems = updateBodyItems)
        )
    }

    private fun updateResponseBodyValue(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: String
    ) {
        val updateBodyItems = updateBodyItems(bodyItems, key, newValue)

        customUiState.value = customUiState.value.copy(
            responseUiState = CustomUiState.ResponseUiState(bodyItems = updateBodyItems)
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
                        builder.url(customUiState.value.fullUrl)
                        customUiState.value
                            .requestUiState
                            .headerKeys
                            .forEachIndexed { index, key ->
                                builder.addHeader(
                                    key,
                                    customUiState.value.requestUiState.headerValues[index]
                                )
                            }

                        customUiState.value
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
            customUiState.value
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