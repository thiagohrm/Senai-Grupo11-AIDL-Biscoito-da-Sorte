package com.senai.grupo11.biscoitodasorte

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.senai.grupo11.biscoitodasorte.service.BiscoitoServiceWrapper
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val TAG = "BiscoitoDaSorte.MainActivity"
    private val service = BiscoitoServiceWrapper()
    private var isTextVisible = false

    private val ANIMATION_DURATION = 500L
    private val RESET_DURATION = 200L
    private val TRANSLATION_X = -570f

    override fun onStart() {
        Log.i(TAG, "onStart()")
        super.onStart()
        service.bind(application)
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy()")
        service.unbind(application)
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fortuneMessageTextView: TextView = findViewById(R.id.fortuneMessageTextView)
        val revealFortuneButton: Button = findViewById(R.id.revealFortuneButton)

        service.state.observe(this) {
            Log.i(TAG, "state: $it")
            if (it) {
                lifecycleScope.launch {
                    service.requestText()
                }
            }
        }

        revealFortuneButton.setOnClickListener {
            if (isTextVisible) {
                hideAndResetTextView(fortuneMessageTextView)
            } else {
                showTextViewWithAnimation(fortuneMessageTextView)
            }
        }

        service.textoDaSorte.observe(this) {
            Log.i(TAG, "textoDaSorte: $it")
            fortuneMessageTextView.text = it
        }
    }

    private fun hideAndResetTextView(view: TextView) {
        view.animate()
            .translationX(0f)
            .setDuration(ANIMATION_DURATION)
            .withEndAction {
                view.visibility = View.INVISIBLE
                isTextVisible = false
                resetTextViewAndAnimateIn(view)
            }
    }

    private fun resetTextViewAndAnimateIn(view: TextView) {
        service.requestText()
        view.visibility = View.INVISIBLE
        view.animate()
            .translationX(0f)
            .setDuration(RESET_DURATION)
            .withEndAction {
                showTextViewWithAnimation(view)
            }
    }

    private fun showTextViewWithAnimation(view: TextView) {
        service.requestText()
        view.visibility = View.VISIBLE
        view.animate()
            .translationX(TRANSLATION_X)
            .setDuration(ANIMATION_DURATION)
            .withEndAction {
                isTextVisible = true
            }
    }
}
