package com.playgroundagc.deepltranslator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.playgroundagc.deepltranslator.model.TranslationText
import com.playgroundagc.deepltranslator.repository.Repository
import com.playgroundagc.deepltranslator.viewmodel.MainViewModel
import com.playgroundagc.deepltranslator.viewmodel.MainViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repository = Repository()

        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val text = findViewById<TextView>(R.id.textView)
        val btn = findViewById<Button>(R.id.button)

        val translate = TranslationText(text.text.toString(), "", "de")

        btn.setOnClickListener {
            viewModel.getTranslation(translate)
        }

        viewModel.translations.observe(this, { response ->
            try {
                Log.i("Response", "Response ? $response")
                text.text = response.toString()
            } catch (e: Exception) {
                Log.e("Response", "Error ? $e")
            }
        })
    }
}