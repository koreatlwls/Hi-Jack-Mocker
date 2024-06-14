package com.koreatlwls.hjm.model

import kotlinx.collections.immutable.ImmutableList

internal sealed interface CustomActions {
    sealed interface Navigates : CustomActions {
        data object Back : Navigates
    }

    sealed interface Updates : CustomActions {
        data class RequestQueryValue(val index: Int, val value: String) : Updates

        data class RequestHeaderValue(val index: Int, val value: String) : Updates

        data class RequestBodyValue(
            val bodyItems: ImmutableList<JsonItem>,
            val key: String,
            val newValue: String,
        ) : Updates

        data class ResponseBodyValue(
            val bodyItems: ImmutableList<JsonItem>,
            val key: String,
            val newValue: String,
        ) : Updates

        data object NewRequest : Updates

        data object NewResponse : Updates

        data object InitClickApi : Updates
    }
}