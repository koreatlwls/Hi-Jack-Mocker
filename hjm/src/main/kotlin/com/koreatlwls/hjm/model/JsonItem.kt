package com.koreatlwls.hjm.model

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import java.util.UUID
import kotlin.random.Random

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
        val expanded: Boolean = false,
    ) : JsonItem {
        val isCanAdd: Boolean = items.isNotEmpty()
    }

    data class ObjectGroup(
        override val id: String = UUID.randomUUID().toString(),
        override val key: String,
        override val isCanDelete: Boolean = false,
        val items: ImmutableList<JsonItem>,
        val expanded: Boolean = false,
    ) : JsonItem
}

internal fun JsonItem.toRandomItem(): JsonItem {
    return when (this) {
        is JsonItem.SingleItem -> this.copy(
            id = UUID.randomUUID().toString(),
            value = getRandomValue(this.value)
        )

        is JsonItem.ArrayGroup -> {
            this.copy(
                id = UUID.randomUUID().toString(),
                items = this.items.map { it.toRandomItem() }.toImmutableList(),
                expanded = true,
            )
        }

        is JsonItem.ObjectGroup -> {
            this.copy(
                id = UUID.randomUUID().toString(),
                items = this.items.map { it.toRandomItem() }.toImmutableList(),
                expanded = true,
            )
        }
    }
}

private fun getRandomValue(value: Any): Any {
    return when (value) {
        is Int -> Random.nextInt()
        is String -> value.toIntOrNull()?.let { Random.nextInt().toString() } ?: value
        is Boolean -> Random.nextBoolean()
        is Double -> Random.nextDouble()
        is Float -> Random.nextFloat()
        is Long -> Random.nextLong()
        else -> value
    }
}