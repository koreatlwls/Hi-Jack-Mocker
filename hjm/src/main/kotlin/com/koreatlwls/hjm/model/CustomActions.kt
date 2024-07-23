package com.koreatlwls.hjm.model

internal sealed interface CustomActions {
    sealed interface Navigates : CustomActions {
        data object Back : Navigates
    }

    sealed interface Updates : CustomActions {
        data object NewRequest : Updates

        data object NewResponse : Updates

        data object InitClickApi : Updates

        data class RequestQueryValue(val index: Int, val value: String) : Updates

        data class RequestHeaderValue(val index: Int, val value: String) : Updates

        data class RequestBodyValue(
            val id: String,
            val newValue: Any,
        ) : Updates

        data class ResponseBodyValue(
            val id: String,
            val newValue: Any,
        ) : Updates

        data class DeleteRequestBodyItem(
            val id: String,
            val index: Int,
        ) : Updates

        data class DeleteResponseBodyItem(
            val id: String,
            val index: Int,
        ) : Updates

        @JvmInline
        value class UpdateRequestBodyExpanded(val id: String) : Updates

        @JvmInline
        value class UpdateResponseBodyExpanded(val id: String) : Updates
    }
}