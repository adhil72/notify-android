package org.notify

import Axios
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.json.JSONObject
import org.notify.activities.main.MainActivity
import org.notify.api.Auth
import org.notify.helpers.Database
import org.notify.helpers.SharedPreferencesManager
import org.notify.models.UserData

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
        app()
    }

    private fun app() {
        val token = SharedPreferencesManager(this@StartActivity).getValue("token", "")
        if (token != "") Axios.setDefaultHeaders(HashMap<String, String>().apply {
            put("Authorization", "Bearer $token")
        })
        if (token != "") Auth.getUserData { responseData ->
            val data = responseData["data"] as JSONObject
            Database(this@StartActivity).insertUserData(
                UserData(
                    username = data["username"].toString(),
                    email = data["email"].toString(),
                    name = data["name"].toString(),
                    messagesSendMonth = data["messagesSendToday"] as JSONObject,
                    messagesSendToday = data["messagesSendMonth"] as JSONObject,
                    clients = data["clients"].toString(),
                    lastAccess = data["lastAccess"].toString()
                )
            )
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun createView(): View {
        return RelativeLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            addView(TextView(this@StartActivity).apply {
                layoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    addRule(RelativeLayout.CENTER_IN_PARENT)
                }
                text = "Notify"
                setTextColor(ContextCompat.getColor(this@StartActivity, R.color.color))
                typeface = Typeface.DEFAULT_BOLD
                textSize = 25f
            })
        }
    }
}