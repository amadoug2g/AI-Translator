package com.playgroundagc.deepltranslator.app.ui

import android.annotation.SuppressLint
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
import com.playgroundagc.deepltranslator.extension.setVisible
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

        setupViews()
        initTimber()
        initViewModel()
        setupSpinners()
        setupListeners()
        setupObservers()
    }
    //endregion

    //region Translation
    private fun translateText() {
        if (binding.textRaw.text.isNotEmpty()) {
            val translationText = binding.textRaw.text
            val sourceLang = binding.sourceLangSpinner.selectedItem as SourceLang
            val targetLang = binding.targetLangSpinner.selectedItem as TargetLang

            checkDetectedLanguage()

            viewModel.formatTranslation("$translationText", sourceLang, targetLang)
        }
    }

    /**
     * Detected Source Language Visibility
     * */
    private fun checkDetectedLanguage() {
        if (binding.textRaw.text.isNotEmpty() || binding.sourceLangSpinner.selectedItemPosition == 0) binding.detectedSourceLanguage.setVisible(
            true
        )
        if (binding.textRaw.text.isEmpty() || binding.sourceLangSpinner.selectedItemPosition != 0) binding.detectedSourceLanguage.setVisible(
            false
        )
    }

    private fun displayTranslation(translation: String?) {
        binding.textTranslated.text = translation
    }

    @SuppressLint("SetTextI18n")
    private fun getDetectedLanguage(response: Response) {
        val detectedLang = response.detected_source_language?.let { SourceLang.valueOf(it) }
        val lang = (detectedLang ?: "${response.detected_source_language}").toString()
        binding.detectedSourceLanguage.text = "Detected language: $lang"
    }
    //endregion

    //region Setups
    private fun setupViews() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            lifecycleOwner = this@MainActivity
            executePendingBindings()
        }
        binding.clearButton.background = null

        binding.detectedSourceLanguage.setVisible(false)

        binding.clearButton.setVisible(false)
    }

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
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            /**
             * Clear Text Button Visibility
             * */
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isEmpty()) binding.clearButton.setVisible(false) else binding.clearButton.setVisible(
                    true
                )

                translateText()
            }
        })

        binding.clearButton.setOnClickListener {
            binding.textRaw.text.clear()
            checkDetectedLanguage()
        }
    }

    private fun setupObservers() {
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

        viewModel.loadingState.observe(this, { state ->
            if (state) progressBarLoadingStart() else progressBarLoadingStop()
        })
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
    private fun showError(message: String? = "") {
        binding.detectedSourceLanguage.setVisible(true)

        Snackbar.make(
            binding.textRaw,
            if (!message.isNullOrEmpty()) message else viewModel.errorMessage.value.toString(),
            Snackbar.LENGTH_LONG
        )
            .setTextColor(Color.WHITE)
            .setActionTextColor(Color.CYAN)
            .show()
    }

    private fun progressBarLoadingStart() {
        binding.progressBar.setVisible(true)
    }

    private fun progressBarLoadingStop() {
        binding.progressBar.setVisible(false)
    }
    //endregion
}