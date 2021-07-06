package com.playgroundagc.deepltranslator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.playgroundagc.deepltranslator.databinding.ActivityMainBinding
import com.playgroundagc.deepltranslator.model.Response
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
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

        binding.detectedSourceLanguage.visibility =
            if (binding.textRaw.text.isNullOrEmpty()) View.GONE else View.VISIBLE

//        binding.cancelButton.background = null

        setupListeners()

        viewModel.translations.observe(this, { response ->
            getDetectedLanguage(response.translations[0])

            try {
                Timber.i("Response ? $response")
                val result = response.translations[0].text
                displayTranslation(result)
            } catch (e: Exception) {
                Timber.e("Error ? $e")
            }
        })
    }

    private fun setupListeners() {
        binding.sourceLangSpinner.adapter =
            CountrySourceAdapter(applicationContext, SourceLang.values())
        binding.targetLangSpinner.adapter =
            CountryTargetAdapter(applicationContext, TargetLang.values())

        binding.targetLangSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (binding.textRaw.text.isNotEmpty()) translateText()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.textRaw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
//                loadingStop()
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
//                binding.translationBtn.setOnClickListener {
//                loadingStart()
                translateText()
//                }
            }
        })

//        binding.cancelButton.setOnClickListener {
//            binding.textRaw.text.clear()
//            Timber.i("clearing field")
//        }

        binding.deleteBtn.setOnClickListener {
            binding.textRaw.text.clear()
            Timber.i("clearing field")
        }
    }

    private fun translateText() {
        val translationText = binding.textRaw.text
        val sourceLang = binding.sourceLangSpinner.selectedItem as SourceLang
        val targetLang = binding.targetLangSpinner.selectedItem as TargetLang

        try {
//            loadingStart()
            if (sourceLang.language == SourceLang.AUTO.language) {
                binding.detectedSourceLanguage.visibility = View.VISIBLE

                viewModel.getTranslation(
                    TranslationText("$translationText", "", TargetLang[targetLang.language]!!)
                )
            } else {
                binding.detectedSourceLanguage.visibility = View.GONE

                viewModel.getTranslation(
                    TranslationText(
                        "$translationText",
                        SourceLang[sourceLang.language]!!,
                        TargetLang[targetLang.language]!!
                    )
                )
            }
//            loadingStop()
        } catch (e: Exception) {
            Timber.e("Error! $e")

//            MaterialAlertDialogBuilder(applicationContext)
//                .setTitle(getString(R.string.create_event))
//                .setMessage(getString(R.string.create_event_confirmation))
//                .setNeutralButton(getString(R.string.confirm_text)) { _: DialogInterface, _: Int ->
//
//                }.show()
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

    private fun getDetectedLanguage(response: Response) {
        val detectedLang = response.detected_source_language?.let { SourceLang.valueOf(it) }
        binding.detectedSourceLanguage.text =
            (detectedLang ?: "${response.detected_source_language}").toString()
    }

    private fun loadingStart() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loadingStop() {
        binding.progressBar.visibility = View.GONE
    }
}