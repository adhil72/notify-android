package org.notify

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.notify.activities.main.MainActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
        app()
    }

    private fun app() {
        startActivity(Intent(this, MainActivity::class.java))
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