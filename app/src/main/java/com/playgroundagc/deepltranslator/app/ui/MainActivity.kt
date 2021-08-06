package com.playgroundagc.deepltranslator.app.ui

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.playgroundagc.deepltranslator.BuildConfig
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.app.framework.RetrofitInstance
import com.playgroundagc.deepltranslator.data.LocalDataSourceImpl
import com.playgroundagc.deepltranslator.data.RemoteDataSourceImpl
import com.playgroundagc.deepltranslator.data.RepositoryImpl
import com.playgroundagc.deepltranslator.databinding.ActivityMainBinding
import com.playgroundagc.deepltranslator.domain.Response
import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.deepltranslator.domain.TargetLang
import com.playgroundagc.deepltranslator.domain.TranslationText
import com.playgroundagc.deepltranslator.usecases.TranslateTextUseCase
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var binding: ActivityMainBinding
        private lateinit var viewModel: MainViewModel
    }

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        initTimber()

        initViewModel()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.clearButton.background = null

        binding.apply {
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }

        viewModel.errorMessage.observe(this, {
            if (it.isNotEmpty()) showError()
        })

        viewModel.translations.observe(this, { response ->
            getDetectedLanguage(response.translations[0])

            try {
                val result = response.translations[0].text
                displayTranslation(result)
            } catch (e: Exception) {
                showError(e.localizedMessage)
            }
        })

        binding.clearButton.setOnClickListener {
            binding.textRaw.text.clear()
        }

        if (binding.textRaw.text.isEmpty() && binding.sourceLangSpinner.selectedItemPosition == 0) {
            binding.detectedSourceLanguage.visibility = View.GONE
        } else {
            binding.detectedSourceLanguage.visibility = View.VISIBLE
        }

        if (binding.textRaw.text.isEmpty()) {
            binding.clearButton.visibility = View.GONE
        } else {
            binding.clearButton.visibility = View.VISIBLE
        }

        setupSpinners()
        setupListeners()
    }
    //endregion

    //region Translation
    private fun setupSpinners() {
        binding.sourceLangSpinner.adapter =
            CountrySourceAdapter(applicationContext, SourceLang.values())
        binding.targetLangSpinner.adapter =
            CountryTargetAdapter(applicationContext, TargetLang.values())
    }

    private fun setupListeners() {
        binding.sourceLangSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    translateText()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.targetLangSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    translateText()
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
                /**
                 * Detected Source Language Visibility
                 * */

                binding.detectedSourceLanguage.visibility =
                    if (binding.textRaw.text.isNotEmpty() && binding.sourceLangSpinner.selectedItemPosition == 0) View.VISIBLE else View.GONE


                /**
                 * Clear Text Button Visibility
                 * */
                binding.clearButton.visibility =
                    if (binding.textRaw.text.isNotEmpty()) View.VISIBLE else View.GONE

                translateText()
            }
        })

        binding.deleteBtn.setOnClickListener {
            binding.textRaw.text.clear()
        }
    }

    private fun translateText() {
        if (binding.textRaw.text.isNotEmpty()) {
//            loadingStart()
            val translationText = binding.textRaw.text
            val sourceLang = binding.sourceLangSpinner.selectedItem as SourceLang
            val targetLang = binding.targetLangSpinner.selectedItem as TargetLang

//            viewModel.queryTranslation(TranslationText("$translationText", SourceLang[sourceLang.language]!!, TargetLang[targetLang.language]!!))

            try {
                when (sourceLang.language) {
                    SourceLang.AUTO.language -> {
                        when (targetLang.language) {
                            TargetLang.EN_GB.language -> {
                                viewModel.queryTranslation(
                                    TranslationText(
                                        "$translationText",
                                        SourceLang[sourceLang.language]!!,
                                        "EN-GB"
                                    )
                                )
                            }
                            TargetLang.EN_US.language -> {
                                viewModel.queryTranslation(
                                    TranslationText(
                                        "$translationText",
                                        SourceLang[sourceLang.language]!!,
                                        "EN-US"
                                    )
                                )
                            }
                            else -> {
                                viewModel.queryTranslation(
                                    TranslationText(
                                        "$translationText",
                                        "",
                                        TargetLang[targetLang.language]!!
                                    )
                                )
                            }
                        }
                    }
                    else -> {
                        when (targetLang.language) {
                            TargetLang.EN_GB.language -> {
                                viewModel.queryTranslation(
                                    TranslationText(
                                        "$translationText",
                                        SourceLang[sourceLang.language]!!,
                                        "EN-GB"
                                    )
                                )
                            }
                            TargetLang.EN_US.language -> {
                                viewModel.queryTranslation(
                                    TranslationText(
                                        "$translationText",
                                        SourceLang[sourceLang.language]!!,
                                        "EN-US"
                                    )
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                showError(e.localizedMessage)
            }

//            loadingStop()
        }
    }

    private fun displayTranslation(translation: String?) {
        try {
            binding.textTranslated.text = translation
        } catch (e: Exception) {
            showError(e.localizedMessage)
//            binding.textTranslated.text = error
        }
    }

    private fun getDetectedLanguage(response: Response) {
        val detectedLang = response.detected_source_language?.let { SourceLang.valueOf(it) }
        binding.detectedSourceLanguage.text =
            (detectedLang ?: "${response.detected_source_language}").toString()
    }
    //endregion

    //region Initialization
    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun initViewModel() {
        val retrofitInstance = RetrofitInstance
        val localDataSource = LocalDataSourceImpl()
        val remoteDataSource = RemoteDataSourceImpl(retrofitInstance)
        val repository = RepositoryImpl(localDataSource, remoteDataSource)
        val translateTextUseCase = TranslateTextUseCase(repository)
        val viewModelFactory = MainViewModelFactory(translateTextUseCase)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }
    //endregion

    //region View Methods
    private fun loadingStart() {
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun loadingStop() {
        binding.progressBar.visibility = View.GONE
    }

    private fun showError(message: String? = "") {
        Snackbar.make(
            binding.textRaw,
            if (!message.isNullOrEmpty()) message else viewModel.errorMessage.value.toString(),
            Snackbar.LENGTH_LONG
        )
//            .setAction("Understood") {
//                viewModel.errorMessage.value
//            }
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.CYAN)
            .show()
    }
    //endregion
}