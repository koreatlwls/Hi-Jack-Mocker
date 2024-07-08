package com.koreatlwls.hjm.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.hjm.HiJackMocker.interceptorManager
import com.koreatlwls.hjm.extensions.parseGroupedListToJSONObject
import com.koreatlwls.hjm.extensions.toApiUiState
import com.koreatlwls.hjm.extensions.toCustomUiState
import com.koreatlwls.hjm.model.ApiActions
import com.koreatlwls.hjm.model.ApiUiState
import com.koreatlwls.hjm.model.CustomActions
import com.koreatlwls.hjm.model.CustomUiState
import com.koreatlwls.hjm.model.JsonItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

internal class HjmViewModel : ViewModel() {

    private val eventList = mutableStateListOf<Pair<String, Response>>()
    val apiUiStateList = mutableStateListOf<ApiUiState>()

    val clickedResponse = mutableStateOf<Response?>(null)

    private val _customUiState = MutableStateFlow(CustomUiState())
    val customUiState: StateFlow<CustomUiState> = _customUiState.asStateFlow()

    private val _onBackEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val onBackEvent = _onBackEvent.asSharedFlow()

    private val _onFinishEvent: MutableSharedFlow<Boolean> = MutableSharedFlow()
    val onFinishEvent = _onFinishEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            interceptorManager.interceptorEvent.collect {
                eventList.add(it)
                apiUiStateList.add(it.second.toApiUiState())
            }
        }
    }

    fun handleApiActions(action: ApiActions.Updates) {
        when (action) {
            is ApiActions.Updates.ClickApi -> clickedResponse(action.index)

            is ApiActions.Updates.DeleteApi -> deleteAndSendResponse(action.index)

            is ApiActions.Updates.DeleteAllApi -> deleteAndSendAllResponse()
        }
    }

    fun handleCustomActions(action: CustomActions.Updates) {
        when (action) {
            CustomActions.Updates.NewRequest -> clickedResponse.value?.let { safeResponse ->
                sendNewRequest(safeResponse)
            }

            CustomActions.Updates.NewResponse -> clickedResponse.value?.let { safeResponse ->
                sendNewResponse(safeResponse)
            }

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

            CustomActions.Updates.InitClickApi -> initClickedResponse()
        }
    }

    private fun clickedResponse(index: Int) {
        clickedResponse.value = eventList[index].second
        _customUiState.value = eventList[index].second.toCustomUiState()
    }

    private fun deleteAndSendResponse(index: Int) {
        viewModelScope.launch {
            val event = eventList[index]
            eventList.removeAt(index)
            apiUiStateList.removeAt(index)
            interceptorManager.sendEventAtResultEvent(event.first, event.second)

            if (eventList.isEmpty()) {
                _onFinishEvent.emit(true)
            }
        }
    }

    private fun deleteAndSendResponse(response: Response, updateResponse: Response) {
        viewModelScope.launch {
            val event = eventList.find { it.second == response }
            val index = eventList.indexOf(event)

            if (event != null) {
                eventList.remove(event)
                apiUiStateList.removeAt(index)
                interceptorManager.sendEventAtResultEvent(event.first, updateResponse)
                initClickedResponse()

                if (eventList.isEmpty()) {
                    _onFinishEvent.emit(true)
                }
            }
        }
    }

    private fun deleteAndSendAllResponse() {
        val job = viewModelScope.launch {
            eventList.forEach { event ->
                interceptorManager.sendEventAtResultEvent(event.first, event.second)
            }
        }

        viewModelScope.launch {
            job.join()
            eventList.clear()
            apiUiStateList.clear()
            _onFinishEvent.emit(true)
        }
    }

    private fun initClickedResponse() {
        clickedResponse.value = null
        _customUiState.value = CustomUiState()
    }

    private fun updateRequestQueryValue(index: Int, value: String) {
        val queryValues = _customUiState.value.apiUiState.queryValues
            .toPersistentList()
            .removeAt(index)
            .add(index, value)
        _customUiState.value = _customUiState.value.copy(
            apiUiState = _customUiState.value.apiUiState.copy(queryValues = queryValues)
        )
    }

    private fun updateRequestHeaderValue(index: Int, value: String) {
        val headerValues = _customUiState.value.requestUiState.headerValues
            .toPersistentList()
            .removeAt(index)
            .add(index, value)
        _customUiState.value = _customUiState.value.copy(
            requestUiState = _customUiState.value.requestUiState.copy(headerValues = headerValues)
        )
    }

    private fun updateRequestBodyValue(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: Any
    ) {
        val updateBodyItems = updateBodyItems(bodyItems, key, newValue)

        _customUiState.value = _customUiState.value.copy(
            requestUiState = _customUiState.value.requestUiState.copy(bodyItems = updateBodyItems)
        )
    }

    private fun updateResponseBodyValue(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: Any
    ) {
        val updateBodyItems = updateBodyItems(bodyItems, key, newValue)

        _customUiState.value = _customUiState.value.copy(
            responseUiState = CustomUiState.ResponseUiState(bodyItems = updateBodyItems)
        )
    }

    private fun updateBodyItems(
        bodyItems: ImmutableList<JsonItem>,
        key: String,
        newValue: Any
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

    private fun sendNewRequest(response: Response) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = response
                .request
                .newBuilder()
                .also { builder ->
                    builder.url(customUiState.value.apiUiState.fullUrl)
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
                val updateResponse = OkHttpClient().newCall(request).execute()
                deleteAndSendResponse(response, updateResponse)
                _onBackEvent.emit(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun sendNewResponse(response: Response) {
        viewModelScope.launch {
            customUiState.value
                .responseUiState
                .bodyItems
                .parseGroupedListToJSONObject()?.let { safeJson ->
                    val updateResponse = response
                        .newBuilder()
                        .body(safeJson.toString().toResponseBody())
                        .build()

                    deleteAndSendResponse(response, updateResponse)
                }

            _onBackEvent.emit(true)
        }
    }

}