package com.example.vamosracharv1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import java.util.*
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private lateinit var totalValueEditText: EditText
    private lateinit var numberOfPeopleEditText: EditText
    private lateinit var calculateButton: Button
    private lateinit var shareButton: Button
    private lateinit var tts: TextToSpeech
    private lateinit var resultTextView: TextView
    private var valorPorPessoa: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalValueEditText = findViewById(R.id.totalValueEditText);
        numberOfPeopleEditText = findViewById(R.id.numberOfPeopleEditText);
        calculateButton = findViewById(R.id.calculateButton);
        shareButton = findViewById(R.id.shareButton);
        resultTextView = findViewById(R.id.textView5);
        tts = TextToSpeech(this, this);

        totalValueEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // não faz nada antes do texto mudar
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // não faz nada quando o texto mudar
                calcularDivisao();
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        numberOfPeopleEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // não faz nada antes do texto mudar
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // não faz nada quando o texto mudar
                calcularDivisao();
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
        calculateButton.setOnClickListener {
            falarResultado("O valor a ser pago por cada pessoas é ${"%.2f".format(valorPorPessoa)}");
        }
        shareButton.setOnClickListener {
            onShareButtonClick(shareButton);
        }
    }

    
    // Método responsável por fazer o cálculo da divisão
    private fun calcularDivisao() {
        val valorTotal = totalValueEditText.text.toString().toDoubleOrNull();
        val numeroPessoas = numberOfPeopleEditText.text.toString().toIntOrNull();

        // Verifica se os campos foram preenchidos corretamente
        if (valorTotal == null || numeroPessoas == null || numeroPessoas == 0) {
            resultTextView.text = "R$ 0,00"
            valorPorPessoa = 0.0
            falarResultado("Preencha todos os campos");
            return
        }
        valorPorPessoa = valorTotal / numeroPessoas

        resultTextView.text = "R$ ${"%.2f".format(valorPorPessoa)}"
    }
    // Método responsável por falar o resultado da divisão
    private fun falarResultado(mensagem: String) {
        tts.speak(mensagem, TextToSpeech.QUEUE_FLUSH, null, "")
    }
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            // Configura a linguagem padrão
            val locale = Locale.getDefault()
            val result = tts.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "A linguagem selecionada não é suportada")
            }
        } else {
            Log.e("TTS", "Falha ao inicializar o TextToSpeech")
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }

    fun onShareButtonClick(view: View) {
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, "O valor final é R$ ${"%.2f".format(valorPorPessoa)}")
        startActivity(Intent.createChooser(shareIntent, "Compartilhar valor final"))
    }


}