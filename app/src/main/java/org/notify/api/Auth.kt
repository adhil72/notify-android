package org.notify.api

import Axios
import org.json.JSONObject

class Auth {
    companion object {

        fun validateAddRequest(data: JSONObject, response: (data: JSONObject) -> Unit) {
            Axios.post("/devices/add/validate", data) { res, err ->
                err?.printStackTrace()
                if (err != null) {
                    throw err
                } else {
                    try {
                        val responseBody = res?.body?.string()
                        response(JSONObject(responseBody))
                    } catch (e: Exception) {
                        throw e
                    }
                }
            }
        }
    }
}