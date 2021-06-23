package com.playgroundagc.deepltranslator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.playgroundagc.deepltranslator.databinding.ActivityMainBinding
import com.playgroundagc.deepltranslator.model.SourceLang
import com.playgroundagc.deepltranslator.model.TargetLang
import com.playgroundagc.deepltranslator.model.TranslationText
import com.playgroundagc.deepltranslator.repository.Repository
import com.playgroundagc.deepltranslator.viewmodel.CountrySourceAdapter
import com.playgroundagc.deepltranslator.viewmodel.CountryTargetAdapter
import com.playgroundagc.deepltranslator.viewmodel.MainViewModel
import com.playgroundagc.deepltranslator.viewmodel.MainViewModelFactory
import timber.log.Timber
import timber.log.Timber.plant


class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: MainViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.apply {
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }

        if (BuildConfig.DEBUG) {
            plant(Timber.DebugTree())
        }

        val repository = Repository()

        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

//        binding.sourceLangSpinner.adapter =
//            ArrayAdapter(applicationContext, R.layout.simple_layout_file, SourceLang.values())
//
//        binding.targetLangSpinner.adapter =
//            ArrayAdapter(applicationContext, R.layout.simple_layout_file, TargetLang.values())
//
//        val sourceList = SourceLang.values()
//        val targetList = TargetLang.values()
//
//        val sourceCountryLang = CountrySourceAdapter(applicationContext, SourceLang.values())
//        val sourceTargetLang = CountryTargetAdapter(applicationContext, TargetLang.values())
//
//        binding.sourceLangSpinner.adapter = sourceCountryLang
        binding.sourceLangSpinner.adapter =
            CountrySourceAdapter(applicationContext, SourceLang.values())
        binding.targetLangSpinner.adapter =
            CountryTargetAdapter(applicationContext, TargetLang.values())

//        binding.sourceLangSpinner.setSelection(0)
//        binding.targetLangSpinner.setSelection(0)

        binding.textRaw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                binding.translationBtn.setOnClickListener {
                    translateText()
                }
            }
        })

        viewModel.translations.observe(this, { response ->
            try {
                Timber.i("Response ? $response")
                val result = response.translations[0].text
                displayTranslation(result)
            } catch (e: Exception) {
                Timber.e("Error ? $e")
            }
        })
    }

    fun translateText() {
        val translationText = binding.textRaw.text
        val sourceLang = binding.sourceLangSpinner.selectedItem as SourceLang
        val targetLang = binding.targetLangSpinner.selectedItem as TargetLang

        Timber.i("Text ? $translationText")
        Timber.i("Source ? $sourceLang")
        Timber.i("Target ? $targetLang")

        try {
            if (sourceLang.language == SourceLang.AUTO.language) {
                Timber.i("AUTO")
                viewModel.getTranslation(
                    TranslationText(
                        "$translationText",
                        "",
                        TargetLang[targetLang.language]!!
                    )
                )
            } else {
                Timber.i("NOT AUTO")
                viewModel.getTranslation(
                    TranslationText(
                        "$translationText",
                        SourceLang[sourceLang.language],
                        TargetLang[targetLang.language]!!
                    )
                )
            }
        } catch (e: Exception) {
            Timber.e("Error! $e")
        }
    }

    private fun displayTranslation(translation: String?) {
        try {
            binding.textTranslated.text = translation
        } catch (e: Exception) {
            val error = "Error! $e"
            binding.textTranslated.text = error
        }
    }
}