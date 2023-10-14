package org.notify.api

import Axios
import android.util.Log
import org.json.JSONObject
import org.notify.helpers.Database

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

        fun updateToken(data: JSONObject,response: (data: JSONObject) -> Unit){
            Axios.post("/devices/update", data) { res, err ->
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

        fun getUserData(response: (data: JSONObject) -> Unit){
            Axios.get("/auth/get/user") { res, err ->
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

        fun disconnectDeviceController(response: () -> Unit){
            Axios.post("/devices/remove", JSONObject()) { res, err ->
                err?.printStackTrace()
                if (err != null) {
                    throw err
                } else {
                   response()
                }
            }
        }
    }
}