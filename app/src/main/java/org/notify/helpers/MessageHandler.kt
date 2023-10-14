package org.notify.helpers

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import org.notify.api.Api

class MessageHandler : FirebaseMessagingService() {

    private val tag = "MessageHandler"
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Utils.sendSMS(this@MessageHandler, message.data["to"],message.data["message"])
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Api.updateToken(JSONObject().apply { put("token",token) }){ Log.e(tag, "onNewToken: token updated" ) }
    }
}