package org.notify.helpers

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.telephony.SmsManager
import android.widget.Toast
import java.util.Locale

class Utils {
    companion object{
        fun deviceName(): String {
            var manufacturer = Build.MANUFACTURER
            var model = Build.MODEL

            manufacturer = manufacturer.substring(0, 1)
                .uppercase(Locale.getDefault()) + manufacturer.substring(1).lowercase(
                Locale.getDefault()
            )
            model =
                model.substring(0, 1).uppercase(Locale.getDefault()) + model.substring(1)
                    .lowercase(Locale.getDefault())

            val deviceName = "$manufacturer $model"
            return deviceName
        }

        fun getAndroidId(context: Context): String {
            return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun sendSMS(context: Context, phoneNumber: String?, message: String?) {
            try {
                val smsManager: SmsManager = context.getSystemService(SmsManager::class.java)
                val sentIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_SENT"), PendingIntent.FLAG_MUTABLE)
                val deliveredIntent = PendingIntent.getBroadcast(context, 0, Intent("SMS_DELIVERED"), PendingIntent.FLAG_MUTABLE)

                // Send the SMS
                smsManager.sendTextMessage(phoneNumber, null, message, sentIntent, deliveredIntent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}