package com.koreatlwls.acr.extensions

import okhttp3.RequestBody
import okhttp3.ResponseBody
import okio.Buffer
import org.json.JSONObject

internal fun ResponseBody.extractResponseJson(): JSONObject {
    val jsonString = this.string()
    return JSONObject(jsonString)
}

internal fun RequestBody.extractRequestJson(): JSONObject {
    val buffer = Buffer()
    writeTo(buffer)
    val jsonString = buffer.readUtf8()
    return JSONObject(jsonString)
}