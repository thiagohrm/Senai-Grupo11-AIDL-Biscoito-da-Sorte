package com.senai.grupo11.biscoitodasorte

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class MainActivity : ComponentActivity() {
    private val fortunes = listOf(
        "Hoje é seu dia de sorte!",
        "A vida é uma jornada, aproveite cada passo.",
        "A felicidade está ao seu alcance.",
        "Você encontrará a resposta em breve.",
        "Uma nova aventura está prestes a começar."
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Inflate o layout XML

        val fortuneMessageTextView: TextView = findViewById(R.id.fortuneMessageTextView)
        val revealFortuneButton: Button = findViewById(R.id.revealFortuneButton)

        revealFortuneButton.setOnClickListener {
            val randomFortune = fortunes.random() // Seleciona uma mensagem aleatória
            fortuneMessageTextView.text = randomFortune
            fortuneMessageTextView.visibility = View.VISIBLE // Torna o TextView visível
        }
    }
}

