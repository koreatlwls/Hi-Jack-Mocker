package com.koreatlwls.acr.model

internal sealed interface ApiActions {
    sealed interface Updates : ApiActions {
        @JvmInline
        value class ClickApi(val index: Int) : Updates

        @JvmInline
        value class DeleteApi(val index: Int) : Updates
    }
}