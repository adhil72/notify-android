package org.notify.helpers

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.notify.api.Auth

class MessageHandler : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e("FCM", "onMessageReceived: $message")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

    }
}