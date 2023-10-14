package org.notify

import Axios
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import org.json.JSONObject
import org.notify.activities.main.MainActivity
import org.notify.api.Api
import org.notify.helpers.Database
import org.notify.helpers.SharedPreferencesManager
import org.notify.models.UserData


class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
    }

    override fun onStart() {
        super.onStart()
        app()
    }

    private fun fetchUserData() {
        Api.getUserData { responseData ->
            val data = responseData["data"] as JSONObject
            Database(this@StartActivity).insertUserData(UserData(username = data["username"].toString(), email = data["email"].toString(), name = data["name"].toString(), messagesSendMonth = data["messagesSendToday"] as JSONObject, messagesSendToday = data["messagesSendMonth"] as JSONObject, clients = data["clients"].toString(), lastAccess = data["lastAccess"].toString()))
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun app() {
        val hasPermission = checkPermissionsGranted()
        if (hasPermission){
            val token = SharedPreferencesManager(this@StartActivity).getValue("token", "")
            if (token != "") Axios.setDefaultHeaders(HashMap<String, String>().apply { put("Authorization", "Bearer $token") })
            if (token != "") fetchUserData()
            else startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun checkPermissionsGranted():Boolean {
        val granted = ContextCompat.checkSelfPermission(this@StartActivity,Manifest.permission.READ_PHONE_STATE) ==PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@StartActivity,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        if (!granted){ ActivityCompat.requestPermissions(this@StartActivity, arrayOf(Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE),1) }
        return granted
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val granted =ContextCompat.checkSelfPermission(this@StartActivity,Manifest.permission.READ_PHONE_STATE) ==PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this@StartActivity,Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
        if(granted)app()
        else{
            MaterialAlertDialogBuilder(this@StartActivity).apply {
                setTitle("Permission needed")
                setMessage("Phone, SMS permissions are needed to run this app")
                setPositiveButton("open settings") { _, _ ->
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = android.net.Uri.fromParts("package", context.packageName, null)
                    intent.data = uri
                    this@StartActivity.startActivity(intent)
                }
                setNegativeButton("Exit app"){_,_ ->this@StartActivity.finish()}
            }.create().show()
        }
    }
    @SuppressLint("SetTextI18n")
    private fun createView(): View {
        return RelativeLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            addView(TextView(this@StartActivity).apply {
                layoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { addRule(RelativeLayout.CENTER_IN_PARENT) }
                text = "Notify"
                setTextColor(ContextCompat.getColor(this@StartActivity, R.color.color))
                typeface = Typeface.DEFAULT_BOLD
                textSize = 25f
            })
        }
    }
}