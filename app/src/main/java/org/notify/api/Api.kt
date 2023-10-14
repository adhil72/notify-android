package org.notify.api

import Axios
import android.util.Log
import org.json.JSONObject

class Api {
    companion object {

        private fun handleError(error: Throwable){
            Log.e("UNEXPECTED ERROR", "handleError",error )
        }

        fun validateAddRequest(data: JSONObject, response: (data: JSONObject) -> Unit) {
            Axios.post("/devices/add/validate", data) { res, err ->
                if (err != null) {
                    handleError(err)
                } else {
                    try {
                        val body = res?.body
                        if(body!=null)response(JSONObject(body.string()))
                    } catch (err: Exception) {
                        handleError(err)
                    }
                }
            }
        }

        fun updateToken(data: JSONObject,response: (data: JSONObject) -> Unit){
            Axios.post("/devices/update", data) { res, err ->
                err?.printStackTrace()
                if (err != null) {
                    handleError(err)
                } else {
                    try {
                        val responseBody = res?.body?.string()
                        if (responseBody!=null)response(JSONObject(responseBody))
                    } catch (err: Exception) {
                        handleError(err)
                    }
                }
            }
        }

        fun getUserData(response: (data: JSONObject) -> Unit){
            Axios.get("/auth/get/user") { res, err ->
                err?.printStackTrace()
                if (err != null) {
                    handleError(err)
                } else {
                    try {
                        val responseBody = res?.body?.string()
                        if(responseBody!=null) response(JSONObject(responseBody))
                    } catch (err: Exception) {
                        handleError(err)
                    }
                }
            }
        }

        fun disconnectDeviceController(response: () -> Unit){
            Axios.post("/devices/remove", JSONObject()) { _, err ->
                err?.printStackTrace()
                if (err != null) {
                    handleError(err)
                } else {
                   response()
                }
            }
        }
    }
}