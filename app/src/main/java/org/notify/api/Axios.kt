import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException

class Axios {

    companion object {

        private var defaultHeaders: MutableMap<String, String> = mutableMapOf()
        private var BASE_URL: String = "http://192.168.88.170:50000"

        private val client = OkHttpClient()

        fun setDefaultHeaders(headers: Map<String, String>) {
            defaultHeaders.clear()
            defaultHeaders.putAll(headers)
        }

        fun get(
            path: String, callback: (Response?, IOException?) -> Unit
        ) {
            val requestBuilder = Request.Builder()
                .url(BASE_URL + path)
                .get()

            for ((key, value) in defaultHeaders) {
                requestBuilder.addHeader(key, value)
            }

            val request = requestBuilder.build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callback(response, null)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback(null, e)
                }
            })
        }

        fun post(
            path: String, jsonBody: JSONObject,
            callback: (Response?, IOException?) -> Unit
        ) {
            val mediaType = "application/json; charset=utf-8".toMediaTypeOrNull()
            val requestBody = jsonBody.toString().toRequestBody(mediaType)

            val requestBuilder = Request.Builder()
                .post(requestBody)
                .url(BASE_URL + path)

            for ((key, value) in defaultHeaders) {
                requestBuilder.addHeader(key, value)
            }

            val request = requestBuilder.build()

            client.newCall(request).enqueue(object : Callback {
                override fun onResponse(call: Call, response: Response) {
                    callback(response, null)
                }

                override fun onFailure(call: Call, e: IOException) {
                    callback(null, e)
                }
            })
        }
    }

}
