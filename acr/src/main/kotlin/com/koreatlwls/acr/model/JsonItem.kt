package com.koreatlwls.acr.model

import kotlinx.collections.immutable.ImmutableList

internal sealed interface JsonItem {
    val key: String

    data class SingleItem(
        override val key: String,
        val value: String
    ) : JsonItem

    data class ArrayGroup(
        override val key: String,
        val items: ImmutableList<JsonItem>
    ) : JsonItem

    data class ObjectGroup(
        override val key: String,
        val items: ImmutableList<JsonItem>
    ) : JsonItem
}