package org.notify.helpers

import android.content.Context
import android.os.Build
import android.provider.Settings
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
    }
}