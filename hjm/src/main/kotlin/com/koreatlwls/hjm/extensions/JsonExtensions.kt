package com.koreatlwls.hjm.extensions

import com.koreatlwls.hjm.model.JsonItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.json.JSONArray
import org.json.JSONObject

internal fun JSONObject?.parseJsonObjectToGroupedList(prefix: String = ""): ImmutableList<JsonItem> {
    if (this == null) return persistentListOf()

    var result = persistentListOf<JsonItem>()

    this.keys().forEach { key ->
        val value = this.get(key)
        val newKey = if (prefix.isEmpty()) key else "$prefix.$key"

        when (value) {
            is JSONObject -> {
                result = result.add(
                    JsonItem.ObjectGroup(
                        newKey,
                        value.parseJsonObjectToGroupedList(newKey)
                    )
                )
            }

            is JSONArray -> {
                var arrayGroup = JsonItem.ArrayGroup(newKey, persistentListOf())
                for (i in 0 until value.length()) {
                    val arrayValue = value.get(i)
                    val arrayKey = "$newKey[$i]"

                    when (arrayValue) {
                        is JSONObject -> {
                            arrayGroup = arrayGroup.copy(
                                items = arrayGroup.items
                                    .toPersistentList()
                                    .add(
                                        JsonItem.ObjectGroup(
                                            arrayKey,
                                            arrayValue.parseJsonObjectToGroupedList(arrayKey)
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
                                            arrayKey,
                                            arrayValue.parseJsonArrayToGroupedList(arrayKey)
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
                                            arrayKey,
                                            arrayValue
                                        )
                                    )
                            )
                        }
                    }
                }
                result = result.add(arrayGroup)
            }

            else -> {
                result = result.add(JsonItem.SingleItem(newKey, value))
            }
        }
    }

    return result
}

private fun JSONArray.parseJsonArrayToGroupedList(prefix: String): ImmutableList<JsonItem> {
    var result = persistentListOf<JsonItem>()

    for (i in 0 until this.length()) {
        val arrayValue = this.get(i)
        val arrayKey = "$prefix[$i]"

        when (arrayValue) {
            is JSONObject -> {
                result = result.add(
                    JsonItem.ObjectGroup(
                        arrayKey,
                        arrayValue.parseJsonObjectToGroupedList(arrayKey)
                    )
                )
            }

            is JSONArray -> {
                result = result.add(
                    JsonItem.ArrayGroup(
                        arrayKey,
                        arrayValue.parseJsonArrayToGroupedList(arrayKey)
                    )
                )
            }

            else -> {
                result = result.add(JsonItem.SingleItem(arrayKey, arrayValue))
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