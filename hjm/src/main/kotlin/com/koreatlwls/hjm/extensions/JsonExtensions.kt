package com.koreatlwls.hjm.extensions

import com.koreatlwls.hjm.model.JsonItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.json.JSONArray
import org.json.JSONObject

internal fun JSONObject?.parseJsonObjectToGroupedList(): ImmutableList<JsonItem> {
    if (this == null) return persistentListOf()

    var result = persistentListOf<JsonItem>()

    this.keys().forEach { key ->
        when (val value = this.get(key)) {
            is JSONObject -> {
                result = result.add(
                    JsonItem.ObjectGroup(
                        key = key,
                        items = value.parseJsonObjectToGroupedList()
                    )
                )
            }

            is JSONArray -> {
                var arrayGroup = JsonItem.ArrayGroup(
                    key = key,
                    items = persistentListOf()
                )

                for (i in 0 until value.length()) {
                    when (val arrayValue = value.get(i)) {
                        is JSONObject -> {
                            arrayGroup = arrayGroup.copy(
                                items = arrayGroup.items
                                    .toPersistentList()
                                    .add(
                                        JsonItem.ObjectGroup(
                                            key = key,
                                            items = arrayValue.parseJsonObjectToGroupedList(),
                                            isCanDelete = true,
                                        )
                                    )
                            )
                        }

                        is JSONArray -> {
                            arrayGroup = arrayGroup.copy(
                                items = arrayGroup.items
                                    .toPersistentList()
                                    .add(
                                        JsonItem.ArrayGroup(
                                            key = key,
                                            items = arrayValue.parseJsonArrayToGroupedList(key),
                                            isCanDelete = true,
                                        )
                                    )
                            )
                        }

                        else -> {
                            arrayGroup = arrayGroup.copy(
                                items = arrayGroup.items
                                    .toPersistentList()
                                    .add(
                                        JsonItem.SingleItem(
                                            key = key,
                                            value = arrayValue,
                                            isCanDelete = true,
                                        )
                                    )
                            )
                        }
                    }
                }
                result = result.add(arrayGroup)
            }

            else -> {
                result = result.add(
                    JsonItem.SingleItem(
                        key = key,
                        value = value
                    )
                )
            }
        }
    }

    return result
}

private fun JSONArray.parseJsonArrayToGroupedList(prefix: String): ImmutableList<JsonItem> {
    var result = persistentListOf<JsonItem>()

    for (i in 0 until this.length()) {
        when (val arrayValue = this.get(i)) {
            is JSONObject -> {
                result = result.add(
                    JsonItem.ObjectGroup(
                        key = prefix,
                        items = arrayValue.parseJsonObjectToGroupedList(),
                        isCanDelete = true,
                    )
                )
            }

            is JSONArray -> {
                result = result.add(
                    JsonItem.ArrayGroup(
                        key = prefix,
                        items = arrayValue.parseJsonArrayToGroupedList(prefix),
                        isCanDelete = true,
                    )
                )
            }

            else -> {
                result = result.add(
                    JsonItem.SingleItem(
                        key = prefix,
                        value = arrayValue,
                        isCanDelete = true,
                    )
                )
            }
        }
    }

    return result
}

internal fun ImmutableList<JsonItem>.parseGroupedListToJSONObject(): JSONObject? {
    if (this.isEmpty()) return null

    val result = JSONObject()

    for (item in this) {
        val key = item.key.substringAfterLast('.')

        when (item) {
            is JsonItem.ObjectGroup -> {
                result.put(key, item.items.parseGroupedListToJSONObject())
            }

            is JsonItem.ArrayGroup -> {
                result.put(key, item.items.parseGroupedListToJSONArray())
            }

            is JsonItem.SingleItem -> {
                result.put(key, item.value)
            }
        }
    }

    return result
}

private fun ImmutableList<JsonItem>.parseGroupedListToJSONArray(): JSONArray {
    val result = JSONArray()

    for (item in this) {
        when (item) {
            is JsonItem.ObjectGroup -> {
                result.put(item.items.parseGroupedListToJSONObject())
            }

            is JsonItem.ArrayGroup -> {
                result.put(item.items.parseGroupedListToJSONObject())
            }

            is JsonItem.SingleItem -> {
                result.put(item.value)
            }
        }
    }

    return result
}