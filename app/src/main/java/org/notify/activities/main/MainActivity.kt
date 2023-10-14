package org.notify.activities.main

import Axios
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.google.firebase.messaging.FirebaseMessaging
import org.json.JSONObject
import org.notify.R
import org.notify.activities.qrcode.CameraActivity
import org.notify.api.Api
import org.notify.helpers.Database
import org.notify.helpers.SharedPreferencesManager
import org.notify.helpers.Utils

@SuppressLint("SetTextI18n")
class MainActivity : AppCompatActivity() {
    private lateinit var statusView: TextView
    private lateinit var infoText: TextView
    private lateinit var connectedLayout: RelativeLayout
    private lateinit var actionButton: MaterialButton
    private lateinit var view: LinearLayout

    private val cameraActivityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val dataString = result.data?.getStringExtra("data")
                if (dataString != null && dataString != "") {
                    val data = JSONObject(dataString)
                    Axios.setDefaultHeaders(HashMap<String, String>().apply { put("Authorization", "Bearer ${data.getString("access")}") })
                    FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                        val body = JSONObject().apply { put("token", token);put("name", Utils.deviceName());put("_id", data.getString("token"));put("uid", Utils.getAndroidId(this@MainActivity)) }
                        Api.validateAddRequest(body) { data ->
                            Log.e("TAG", ": $data")
                            if (data.getString("message").equals("device already added") || data.getString("message").equals("device added")) {
                                SharedPreferencesManager(this@MainActivity).saveValue("token", data.getJSONObject("data").getString("token"))
                                this@MainActivity.runOnUiThread { onConnected() }
                            } else AlertDialog.Builder(this@MainActivity).apply { setMessage("Some problems in login in") }.create().show()
                        }
                    }
                }
            }
        }

    private fun onConnected() {

        if (SharedPreferencesManager(this@MainActivity).getValue("token", "it is null") == "it is null") {
            connectedLayout.visibility = View.GONE
            actionButton.visibility = View.VISIBLE
            infoText.text = "tap to connect"
            statusView.text = "inactive"
            statusView.setTextColor(Color.RED)
        } else {
            connectedLayout.visibility = View.VISIBLE
            actionButton.visibility = View.GONE
            infoText.text = "connected"
            statusView.text = "active"
            statusView.setTextColor(Color.GREEN)
        }
    }

    override fun onStart() {
        super.onStart()
        onConnected()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
        app()
    }

    private fun app() {
        actionButton.setOnClickListener { cameraActivityResultLauncher.launch(Intent(this, CameraActivity::class.java))}
        connectedLayout.setOnClickListener {
            MaterialAlertDialogBuilder(this@MainActivity).apply {
                    setTitle("Disconnect from server")
                    setMessage("Confirm to disconnect from server")
                    setPositiveButton("Confirm") { _, _ ->
                        Api.disconnectDeviceController {
                            Axios.setDefaultHeaders(HashMap())
                            this@MainActivity.runOnUiThread { SharedPreferencesManager(this@MainActivity).deleteKey("token");onConnected() }
                        }
                    }
                    setNegativeButton("Cancel") { dialogue, _ -> dialogue.dismiss() } }.create().show()
        }
    }

    private fun createView(): View {
        view = LinearLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.background))
            orientation = LinearLayout.VERTICAL
            val p = 50
            setPadding(p, 0, p, 0)
            addView(createTopView())
            addView(createTitle())
            addView(createContent())
        }
        return view
    }

    private fun createContent(): View {
        return LinearLayout(this@MainActivity).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply { topMargin = -300 }
            gravity = Gravity.CENTER
            orientation = LinearLayout.VERTICAL
            infoText = TextView(this@MainActivity).apply {
                text = "Connect"
                typeface = Typeface.DEFAULT_BOLD
                textSize = 30f
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                gravity = Gravity.CENTER
            }
            addView(infoText)
            connectedLayout = RelativeLayout(this@MainActivity).apply {
                visibility = View.GONE
                val iconSize = resources.getDimensionPixelSize(R.dimen.icon_size)
                layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply { gravity = Gravity.CENTER }
                background = GradientDrawable().apply { setStroke(10, Color.GREEN);cornerRadius = 1000f }
                addView(ImageView(this@MainActivity).apply { setImageResource(R.drawable.icon_cloud_done_foreground);layoutParams = RelativeLayout.LayoutParams((iconSize / 1.7).toInt(), (iconSize / 1.7).toInt()).apply {addRule(RelativeLayout.CENTER_IN_PARENT) } })
            }
            addView(connectedLayout)
            actionButton = MaterialButton(this@MainActivity).apply {
                val iconSize = resources.getDimensionPixelSize(R.dimen.icon_size)
                icon = AppCompatResources.getDrawable(this@MainActivity, R.drawable.icon_power_foreground)
                layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply { gravity = Gravity.CENTER }
                iconGravity = MaterialButton.ICON_GRAVITY_TEXT_TOP
                setIconSize(iconSize - (iconSize * 0.4).toInt())
            }
            addView(actionButton)
            addView(LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply {
                    topMargin = 50
                }
                gravity = Gravity.CENTER

                addView(LinearLayout(this@MainActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(MaterialTextView(this@MainActivity).apply {
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
                        typeface = Typeface.DEFAULT_BOLD
                        text = "${Database(this@MainActivity).getUserData()?.messagesSendToday?.getString("count")} SMS" })
                    addView(MaterialTextView(this@MainActivity).apply { text = "send today" })
                })

                addView(View(this@MainActivity).apply { layoutParams = LinearLayout.LayoutParams(2, MATCH_PARENT).apply { leftMargin = 30;rightMargin = 30 };setBackgroundColor(Color.GRAY) })

                addView(LinearLayout(this@MainActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_HORIZONTAL
                    addView(MaterialTextView(this@MainActivity).apply {
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
                        typeface = Typeface.DEFAULT_BOLD
                        text = "${Database(this@MainActivity).getUserData()?.messagesSendMonth?.getString("count")} SMS"
                    })
                    addView(MaterialTextView(this@MainActivity).apply {text = "send this month" })
                })
                addView(View(this@MainActivity).apply { layoutParams = LinearLayout.LayoutParams(2, MATCH_PARENT).apply { leftMargin = 30;rightMargin = 30 };setBackgroundColor(Color.GRAY) })
                addView(LinearLayout(this@MainActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(MaterialTextView(this@MainActivity).apply {
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
                        typeface = Typeface.DEFAULT_BOLD
                        text = "${Database(this@MainActivity).getUserData()?.clients} Clients"
                    })
                    addView(MaterialTextView(this@MainActivity).apply { text = "connected" })
                })
            })
        }
    }

    private fun createTitle(): View {
        return TextView(this@MainActivity).apply {
            typeface = Typeface.DEFAULT_BOLD
            textSize = 25f
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            text = "Notify"
        }
    }

    private fun createTopView(): View {
        return RelativeLayout(this@MainActivity).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
            addView(TextView(this@MainActivity).apply {
                text = Utils.deviceName().uppercase()
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
            })
            statusView = TextView(this@MainActivity).apply {
                text = "Inactive"
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.RED)
                layoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { addRule(RelativeLayout.ALIGN_PARENT_END) }
            }
            addView(statusView)
        }
    }
}