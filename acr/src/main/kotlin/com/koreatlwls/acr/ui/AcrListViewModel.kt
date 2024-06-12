package com.koreatlwls.acr.ui

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.koreatlwls.acr.extensions.toApiUiState
import com.koreatlwls.acr.extensions.toCustomUiState
import com.koreatlwls.acr.model.ApiUiState
import com.koreatlwls.acr.model.CustomUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
internal class AcrListViewModel @Inject constructor(
    @Named("send") private val sendChannel: Channel<Response>,
    @Named("receive") private val receiveChannel: Channel<Response>,
) : ViewModel() {

    private val responseList = mutableStateListOf<Response>()
    val apiUiStateList = mutableStateListOf<ApiUiState>()

    private val _clickedResponse : MutableStateFlow<Response?> = MutableStateFlow(null)

    val customUiState = _clickedResponse
        .filterNotNull()
        .map { it.toCustomUiState() }
        .stateIn(
            scope = viewModelScope,
            started= SharingStarted.WhileSubscribed(),
            initialValue = CustomUiState()
        )

    init {
        viewModelScope.launch {
            sendChannel.consumeEach {
                responseList.add(it)
                apiUiStateList.add(it.toApiUiState())
            }
        }
    }

}