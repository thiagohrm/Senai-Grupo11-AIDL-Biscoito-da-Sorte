package com.senai.grupo11.biscoitodasorte

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.senai.grupo11.biscoitodasorte.service.BiscoitoServiceWrapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val TAG = "BiscoitoDaSorte.MainActivity"
    private val service = BiscoitoServiceWrapper()

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
        setContentView(R.layout.activity_main) // Inflate o layout XML
        service.state.observe(this){
            Log.i(TAG, "state: $it")
            if (it) {
                lifecycleScope.launch {
                    service.requestText()
                }
            }
        }
        val fortuneMessageTextView: TextView = findViewById(R.id.fortuneMessageTextView)
        val revealFortuneButton: Button = findViewById(R.id.revealFortuneButton)

        revealFortuneButton.setOnClickListener {
            fortuneMessageTextView.text = ""
            fortuneMessageTextView.visibility = View.VISIBLE // Torna o TextView visível
            service.requestText()
        }
        service.textoDaSorte.observe(this){
            Log.i(TAG, "textoDaSorte: $it")
            revealFortuneButton.text = it
        }
    }
}

