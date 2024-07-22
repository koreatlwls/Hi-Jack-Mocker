package com.koreatlwls.hjm.model

import kotlinx.collections.immutable.ImmutableList
import java.util.UUID

internal sealed interface JsonItem {
    val id: String
    val key: String
    val isCanDelete: Boolean

    data class SingleItem(
        override val id: String = UUID.randomUUID().toString(),
        override val key: String,
        override val isCanDelete: Boolean = false,
        val value: Any,
    ) : JsonItem

    data class ArrayGroup(
        override val id: String = UUID.randomUUID().toString(),
        override val key: String,
        override val isCanDelete: Boolean = false,
        val items: ImmutableList<JsonItem>,
    ) : JsonItem {
        val isCanAdd: Boolean = items.isNotEmpty()
    }

    data class ObjectGroup(
        override val id: String = UUID.randomUUID().toString(),
        override val key: String,
        override val isCanDelete: Boolean = false,
        val items: ImmutableList<JsonItem>,
    ) : JsonItem
}