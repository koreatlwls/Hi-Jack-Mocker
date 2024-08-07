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

        data class UpdateBodyValue(
            val isRequestBody: Boolean,
            val id: String,
            val newValue: Any,
        ) : Updates

        data class DeleteBodyItem(
            val isRequestBody: Boolean,
            val id: String,
            val index: Int,
        ) : Updates


        data class UpdateBodyExpanded(
            val isRequestBody: Boolean,
            val id: String
        ) : Updates

        data class AddBodyItem(
            val isRequestBody: Boolean,
            val id: String,
        ) : Updates
    }
}