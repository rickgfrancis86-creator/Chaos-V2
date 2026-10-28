package com.chaosv2

import android.app.Activity
import android.os.Bundle
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : Activity() {

    private val black = Color.rgb(9, 9, 11)
    private val panel = Color.rgb(20, 20, 23)
    private val red = Color.rgb(175, 28, 42)
    private val white = Color.rgb(225, 225, 225)
    private val grey = Color.rgb(130, 130, 135)

    private val quotes = listOf(
        "\"The only true wisdom is in knowing you know nothing.\" — Socrates (attributed)",
        "\"Knowledge is power.\" — Francis Bacon (commonly attributed)",
        "\"The greater the knowledge, the greater the doubt.\" — Goethe (attributed)",
        "\"It is the mark of an educated mind to be able to entertain a thought without accepting it.\" — Aristotle (paraphrased)",
        "\"The more I learn, the more I realize how much I don't know.\" — commonly attributed to Einstein",
        "\"Beware of false knowledge; it is more dangerous than ignorance.\" — commonly attributed to George Bernard Shaw",
        "\"Science is organized knowledge.\" — Herbert Spencer",
        "\"Wonder is the beginning of wisdom.\" — Socrates (attributed)"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val prefs = getSharedPreferences("chaos_preferences", MODE_PRIVATE)
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
        val lastQuoteDay = prefs.getString("quote_day", "")

        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(22, 28, 22, 20)
            setBackgroundColor(black)
        }

        val title = TextView(this).apply {
            text = "CHAOS V2"
            textSize = 28f
            typeface = Typeface.create("sans-serif", Typeface.BOLD)
            gravity = Gravity.CENTER
            setTextColor(red)
        }

        val subtitle = TextView(this).apply {
            text = "PERSONAL INTELLIGENCE"
            textSize = 11f
            gravity = Gravity.CENTER
            setTextColor(grey)
            setPadding(0, 5, 0, 22)
        }

        val quote = TextView(this).apply {
            val index = if (lastQuoteDay == today) {
                prefs.getInt("quote_index", 0).coerceIn(quotes.indices)
            } else {
                val previous = prefs.getInt("quote_index", -1)
                val next = (previous + 1) % quotes.size
                prefs.edit()
                    .putString("quote_day", today)
                    .putInt("quote_index", next)
                    .apply()
                next
            }

            text = quotes[index]
            textSize = 13f
            gravity = Gravity.CENTER
            setTextColor(grey)
            setPadding(8, 0, 8, 18)
        }

        val warning = TextView(this).apply {
            text = "NOTICE  •  AI ENGINE NOT CONNECTED"
            textSize = 11f
            setTextColor(red)
            setPadding(0, 0, 0, 14)
        }

        val conversation = TextView(this).apply {
            text = "Chaos V2\n\nThe interface is ready.\n\nAI engine: Disconnected\nWeb research: Not configured\nPersistent knowledge: Not configured"
            textSize = 15f
            typeface = Typeface.create("sans-serif", Typeface.NORMAL)
            setTextColor(white)
            setPadding(16, 18, 16, 18)
            setBackgroundColor(panel)
        }

        val input = EditText(this).apply {
            hint = "Ask a question..."
            textSize = 15f
            setTextColor(white)
            setHintTextColor(grey)
            setPadding(14, 12, 14, 12)
            setBackgroundColor(panel)
        }

        val send = Button(this).apply {
            text = "SEND"
            setTextColor(white)
            setBackgroundColor(red)

            setOnClickListener {
                val question = input.text.toString().trim()
                if (question.isNotEmpty()) {
                    conversation.text =
                        "YOU\n$question\n\n" +
                        "CHAOS V2\nThe AI engine is not connected yet. " +
                        "Your question has not been sent to an AI service."
                    input.text.clear()
                }
            }
        }

        root.addView(title)
        root.addView(subtitle)
        root.addView(quote)
        root.addView(warning)

        root.addView(
            conversation,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f
            ).apply {
                bottomMargin = 14
            }
        )

        root.addView(input)

        root.addView(
            send,
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                topMargin = 8
            }
        )

        setContentView(root)

        val lastAnimation = prefs.getLong("last_animation", 0L)
        val now = System.currentTimeMillis()

        if (now - lastAnimation >= 24 * 60 * 60 * 1000L) {
            root.alpha = 0f
            root.animate()
                .alpha(1f)
                .setDuration(700)
                .setInterpolator(DecelerateInterpolator())
                .start()

            prefs.edit().putLong("last_animation", now).apply()
        }
    }
}
