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
import androidx.databinding.library.BuildConfig
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.snackbar.Snackbar
import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.app.framework.RetrofitInstance
import com.playgroundagc.deepltranslator.data.LocalDataSourceImpl
import com.playgroundagc.deepltranslator.data.RemoteDataSourceImpl
import com.playgroundagc.deepltranslator.data.RepositoryImpl
import com.playgroundagc.deepltranslator.databinding.ActivityMainBinding
import com.playgroundagc.deepltranslator.databinding.MainActivityBinding
import com.playgroundagc.deepltranslator.domain.Response
import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.deepltranslator.domain.TargetLang
import com.playgroundagc.deepltranslator.extension.setVisible
import com.playgroundagc.deepltranslator.usecases.TranslateTextUseCase
import timber.log.Timber

private const val PREFS_NAME = "languageCheck"

class MainActivity : AppCompatActivity() {

    companion object {
        private lateinit var binding: ActivityMainBinding
        private lateinit var bindingBis: MainActivityBinding
        private lateinit var viewModel: MainViewModel
    }
        lateinit var navController: NavController

    //region Override Methods
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        navController = navHostFragment.navController

        //setupActionBarWithNavController(findNavController(R.id.fragment))

        //setupViews()
        initTimber()
        //initViewModel()
        setupSpinners()
        setupListeners()
        setupObservers()

        restoreLanguages()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onStop() {
        super.onStop()
        saveSourceLanguage()
        saveTargetLanguage()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragment)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    //endregion

    //region Translation
    private fun translateText() {
        if (binding.textRaw.text.isNotEmpty()) {
            val translationText = binding.textRaw.text
            val sourceLang = binding.sourceLangSpinner.selectedItem as SourceLang
            val targetLang = binding.targetLangSpinner.selectedItem as TargetLang

            checkRawInput()

            viewModel.formatTranslation("$translationText", sourceLang, targetLang)
        }
    }

    /**
     * Detected Source Language & Clear Text Visibility
     * */
    private fun checkRawInput() {
        if (binding.textRaw.text.isNotEmpty() || binding.sourceLangSpinner.selectedItemPosition == 0) binding.detectedSourceLanguage.setVisible(
            true
        )

        if (binding.textRaw.text.isEmpty() || binding.sourceLangSpinner.selectedItemPosition != 0) binding.detectedSourceLanguage.setVisible(
            false
        )

        if (binding.textRaw.text.isEmpty()) binding.clearButton.setVisible(false) else binding.clearButton.setVisible(
            true
        )
    }

    /**
     * Switch Language Visibility
     * */
    private fun checkSwitchLang() {
        if (binding.sourceLangSpinner.selectedItemPosition != 0) {
            binding.switchButton.isEnabled = true
            binding.switchButton.visibility = View.VISIBLE
        } else {
            binding.switchButton.isEnabled = false
            binding.switchButton.visibility = View.GONE
        }
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
        binding.switchButton.background = null

        binding.detectedSourceLanguage.setVisible(false)

        binding.clearButton.setVisible(false)

        checkSwitchLang()
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

                    viewModel.addSourceLang(p2)

                    checkSwitchLang()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.targetLangSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    translateText()

                    viewModel.addTargetLang(p2)

                    checkSwitchLang()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

        binding.textRaw.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                checkRawInput()

                translateText()
            }
        })

        binding.clearButton.setOnClickListener {
            binding.textRaw.text.clear()
            checkRawInput()
        }

        binding.switchButton.setOnClickListener {
            val tempSource = binding.targetLangSpinner.selectedItemPosition
            val tempTarget = binding.sourceLangSpinner.selectedItemPosition

            setSourceLanguage(tempSource)
            setTargetLanguage(tempTarget)
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

    //region Language
    private fun saveSourceLanguage() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("lastSource", viewModel.lastUsedSourceLang.value ?: 0)
        editor.apply()
    }

    private fun saveTargetLanguage() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt("lastTarget", viewModel.lastUsedTargetLang.value ?: 0)
        editor.apply()
    }

    private fun restoreLanguages() {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)

        val sourceLang = prefs.getInt(
            "lastSource",
            viewModel.lastUsedSourceLang.value ?: binding.sourceLangSpinner.selectedItemPosition
        )
        val targetLang = prefs.getInt(
            "lastTarget",
            viewModel.lastUsedTargetLang.value ?: binding.targetLangSpinner.selectedItemPosition
        )

        binding.sourceLangSpinner.setSelection(sourceLang)
        binding.targetLangSpinner.setSelection(targetLang)
    }

    private fun setTargetLanguage(sourceIndex: Int) {
        when (sourceIndex) {
            1 -> {
                binding.targetLangSpinner.setSelection(0)
            }
            2 -> {
                binding.targetLangSpinner.setSelection(1)
            }
            3 -> {
                binding.targetLangSpinner.setSelection(2)
            }
            4 -> {
                binding.targetLangSpinner.setSelection(3)
            }
            5 -> {
                binding.targetLangSpinner.setSelection(4)
            }
            6 -> {
                binding.targetLangSpinner.setSelection(5)
            }
            7 -> {
                binding.targetLangSpinner.setSelection(8)
            }
            8 -> {
                binding.targetLangSpinner.setSelection(9)
            }
            9 -> {
                binding.targetLangSpinner.setSelection(10)
            }
            10 -> {
                binding.targetLangSpinner.setSelection(11)
            }
            11 -> {
                binding.targetLangSpinner.setSelection(12)
            }
            12 -> {
                binding.targetLangSpinner.setSelection(13)
            }
            13 -> {
                binding.targetLangSpinner.setSelection(14)
            }
            14 -> {
                binding.targetLangSpinner.setSelection(15)
            }
            15 -> {
                binding.targetLangSpinner.setSelection(16)
            }
            16 -> {
                binding.targetLangSpinner.setSelection(17)
            }
            17 -> {
                binding.targetLangSpinner.setSelection(18)
            }
            18 -> {
                binding.targetLangSpinner.setSelection(19)
            }
            19 -> {
                binding.targetLangSpinner.setSelection(21)
            }
            20 -> {
                binding.targetLangSpinner.setSelection(22)
            }
            21 -> {
                binding.targetLangSpinner.setSelection(23)
            }
            22 -> {
                binding.targetLangSpinner.setSelection(24)
            }
            23 -> {
                binding.targetLangSpinner.setSelection(25)
            }
            24 -> {
                binding.targetLangSpinner.setSelection(26)
            }
        }
    }

    private fun setSourceLanguage(targetIndex: Int) {
        when (targetIndex) {
            0 -> {
                binding.sourceLangSpinner.setSelection(1)
            }
            1 -> {
                binding.sourceLangSpinner.setSelection(2)
            }
            2 -> {
                binding.sourceLangSpinner.setSelection(3)
            }
            3 -> {
                binding.sourceLangSpinner.setSelection(4)
            }
            4 -> {
                binding.sourceLangSpinner.setSelection(5)
            }
            5 -> {
                binding.sourceLangSpinner.setSelection(6)
            }
            6 -> {
                binding.sourceLangSpinner.setSelection(6)
            }
            7 -> {
                binding.sourceLangSpinner.setSelection(6)
            }
            8 -> {
                binding.sourceLangSpinner.setSelection(7)
            }
            9 -> {
                binding.sourceLangSpinner.setSelection(8)
            }
            10 -> {
                binding.sourceLangSpinner.setSelection(9)
            }
            11 -> {
                binding.sourceLangSpinner.setSelection(10)
            }
            12 -> {
                binding.sourceLangSpinner.setSelection(11)
            }
            13 -> {
                binding.sourceLangSpinner.setSelection(12)
            }
            14 -> {
                binding.sourceLangSpinner.setSelection(13)
            }
            15 -> {
                binding.sourceLangSpinner.setSelection(14)
            }
            16 -> {
                binding.sourceLangSpinner.setSelection(15)
            }
            17 -> {
                binding.sourceLangSpinner.setSelection(16)
            }
            18 -> {
                binding.sourceLangSpinner.setSelection(17)
            }
            19 -> {
                binding.sourceLangSpinner.setSelection(18)
            }
            20 -> {
                binding.sourceLangSpinner.setSelection(18)
            }
            21 -> {
                binding.sourceLangSpinner.setSelection(19)
            }
            22 -> {
                binding.sourceLangSpinner.setSelection(20)
            }
            23 -> {
                binding.sourceLangSpinner.setSelection(21)
            }
            24 -> {
                binding.sourceLangSpinner.setSelection(22)
            }
            25 -> {
                binding.sourceLangSpinner.setSelection(23)
            }
            26 -> {
                binding.sourceLangSpinner.setSelection(24)
            }
        }
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
        //val viewModelFactory = MainViewModelFactory(translateTextUseCase)
        //viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
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