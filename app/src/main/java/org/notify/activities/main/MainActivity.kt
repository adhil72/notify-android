package org.notify.activities.main

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import org.notify.R

class MainActivity : AppCompatActivity() {
    private lateinit var view: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView())
        window.statusBarColor = ContextCompat.getColor(this, R.color.background)
    }

    private fun createView(): View {
        view = LinearLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setBackgroundColor(ContextCompat.getColor(this@MainActivity, R.color.background))
            orientation = LinearLayout.VERTICAL
            var p = 50
            setPadding(p, 0, p, 0)

            addView(createTopView())
            addView(createTitle())
            addView(createContent())
        }
        return view
    }

    private fun createContent(): View {
        return LinearLayout(this@MainActivity).apply {
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT).apply {
                topMargin = -300
            }
            gravity = Gravity.CENTER
            orientation = LinearLayout.VERTICAL

            addView(TextView(this@MainActivity).apply {
                text = "Connect"
                typeface = Typeface.DEFAULT_BOLD
                textSize = 30f
                layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT)
                gravity = Gravity.CENTER
            })
            addView(MaterialButton(this@MainActivity).apply {
                val iconSize =
                    resources.getDimensionPixelSize(R.dimen.icon_size) // Define your icon size in resources (e.g., dimens.xml)

                icon = AppCompatResources.getDrawable(
                    this@MainActivity,
                    R.drawable.icon_power_foreground
                )
                layoutParams = LinearLayout.LayoutParams(iconSize, iconSize).apply {
                    gravity = Gravity.CENTER
                }

                // Center the icon horizontally and vertically within the button
                iconGravity = MaterialButton.ICON_GRAVITY_TEXT_TOP
                setIconSize(iconSize - (iconSize * 0.4).toInt())

            })

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
                        text = "100 SMS"
                    })

                    addView(MaterialTextView(this@MainActivity).apply {
                        text = "send today"
                    })
                })

                addView(View(this@MainActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(2, MATCH_PARENT).apply {
                        leftMargin = 30
                        rightMargin = 30
                    }
                    setBackgroundColor(Color.GRAY)
                })

                addView(LinearLayout(this@MainActivity).apply {
                    orientation = LinearLayout.VERTICAL
                    gravity = Gravity.CENTER_HORIZONTAL
                    addView(MaterialTextView(this@MainActivity).apply {
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
                        typeface = Typeface.DEFAULT_BOLD
                        text = "1000 SMS"
                    })

                    addView(MaterialTextView(this@MainActivity).apply {
                        text = "send this month"
                    })
                })

                addView(View(this@MainActivity).apply {
                    layoutParams = LinearLayout.LayoutParams(2, MATCH_PARENT).apply {
                        leftMargin = 30
                        rightMargin = 30
                    }
                    setBackgroundColor(Color.GRAY)
                })

                addView(LinearLayout(this@MainActivity).apply {
                    orientation = LinearLayout.VERTICAL

                    addView(MaterialTextView(this@MainActivity).apply {
                        setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
                        typeface = Typeface.DEFAULT_BOLD
                        text = "100 SMS"
                    })

                    addView(MaterialTextView(this@MainActivity).apply {
                        text = "send this year"
                    })
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
                text = "PIXEL 4a 5G"
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(ContextCompat.getColor(this@MainActivity, R.color.color))
            })

            addView(TextView(this@MainActivity).apply {
                text = "Inactive"
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.RED)
                layoutParams = RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_END)
                }
            })
        }
    }
}