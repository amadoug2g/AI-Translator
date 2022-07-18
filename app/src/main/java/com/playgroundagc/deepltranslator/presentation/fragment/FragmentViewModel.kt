package com.playgroundagc.deepltranslator.presentation.fragment

import androidx.lifecycle.ViewModel
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.core.data.TargetLang
import com.playgroundagc.core.data.TranslationUiState
import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Created by Amadou on 16/07/2022, 01:00
 *
 * Purpose: Fragments' ViewModel
 *
 */

class FragmentViewModel(
    private val translateText: TranslateTextUC,
    private val getAPIUsage: GetAPIUsageUC
) : ViewModel() {

    //region Variables
    private val coroutineScope = CoroutineScope(IO)

    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()
    //endregion

    //region Setters
    fun setSourceLanguage(source: SourceLang) {
        _uiState.update { state -> state.copy(sourceLang = source) }
    }

    fun setTargetLanguage(target: TargetLang) {
        _uiState.update { state -> state.copy(targetLang = target) }
    }

    fun setInputText(text: String) {
        _uiState.update { state -> state.copy(inputText = text) }
    }

    private fun setOutputText(text: String) {
        _uiState.update { state -> state.copy(outputText = text) }
    }

    private fun setLoadingStatus(status: Boolean) {
        _uiState.update { state -> state.copy(isFetchingTranslation = status) }
    }

    private fun setErrorMessage(message: String = "") {
        _uiState.update { state -> state.copy(errorMessage = message) }
        setLoadingStatus(false)
    }

    fun resetState() {
        _uiState.update { state ->
            state.copy(
                isFetchingTranslation = false,
                isDetectingLanguage = true,
                sourceLang = SourceLang.AUTO,
                targetLang = TargetLang.BG,
                inputText = "",
                outputText = "",
                errorMessage = ""
            )
        }
    }
    //endregion

    //region Translation
    suspend fun translation() {
        setLoadingStatus(true)
        try {
            val query = uiState.value.toQuery()

            val response = translateText.invoke(query)

            if (response.isSuccessful) {
                setOutputText(response.body()?.translations?.get(0)?.text.toString())

                Timber.d("Translation response success1?: ${response.body()?.translations}")
                Timber.d("Translation response success22: ${response.body()?.translations?.get(1)?.text.toString()}")
            } else {
                Timber.d("Translation response failure: ${response.body()}")
            }
            setLoadingStatus(false)
        } catch (e: Exception) {
            Timber.d("Translation errors: ${uiState.value}")
            setLoadingStatus(false)
        }
    }

    fun getUsage() {
        setLoadingStatus(true)
        coroutineScope.launch {
            val result = getAPIUsage.invoke()

            if (result.isSuccessful) {
                Timber.d("Success usage: ${result.body()}")
            } else {
                Timber.d("Failed reason: ${result.message()}")
            }
        }
        setLoadingStatus(false)
    }
    //endregion
}
