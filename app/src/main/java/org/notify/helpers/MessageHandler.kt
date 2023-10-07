package org.notify.helpers

import android.app.AlertDialog
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.json.JSONObject
import org.notify.api.Auth

class MessageHandler : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Utils.sendSMS(this@MessageHandler, message.data["to"],message.data["message"])
    }

    override fun onNewToken(token: String) {
        var body = JSONObject().apply {
            put("token", token)
            put("name", Utils.deviceName())
            put("_id", SharedPreferencesManager(this@MessageHandler).getValue("_id",""))
            put("uid", Utils.getAndroidId(this@MessageHandler))
        }
        super.onNewToken(token)
    }
}