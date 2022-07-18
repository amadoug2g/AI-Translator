package com.playgroundagc.deepltranslator.presentation.fragment

import androidx.lifecycle.ViewModel
import com.playgroundagc.core.data.*
import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Response
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
    private val coroutineScope = CoroutineScope(IO)

    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()

    fun setSourceLanguage(source: SourceLang) {
        _uiState.update { state -> state.copy(sourceLang = source) }
    }

    fun setTargetLanguage(target: TargetLang) {
        _uiState.update { state -> state.copy(targetLang = target) }
    }

    fun translateText(translationQuery: TranslationQuery = uiState.value.toQuery()) {
        coroutineScope.launch {
            val result = translateText.invoke(translationQuery)
            handleResponse(result)
        }
    }

    private fun handleResponse(response: Response<TranslationResponse>) {
        when (response.isSuccessful) {
            true -> {
                val result = response.body()
                if (result != null) {
                    Timber.d("Translation success: ${result.text}")
                } else {
                    Timber.d("Translation null: ${result?.text}")
                }
            }
            else -> {
                Timber.d("Translation not successful")
            }
        }
    }

    suspend fun translation() {
        try {
            val query = uiState.value.toQuery()

            val response = translateText.invoke(query)

            if (response.isSuccessful) {
                Timber.d("Translation response1: ${response.body()}")
            } else {
                Timber.d("Translation response2: ${response.body()}")
            }
        } catch (e: Exception) {
            Timber.d("Translation errors: ${uiState.value}")
        }
    }

    fun getUsage() {
        coroutineScope.launch {
            val result = getAPIUsage.invoke()

            if (result.isSuccessful) {
                Timber.d("Success usage: ${result.body()}")
            } else {
                Timber.d("Failed reason: ${result.message()}")
            }
        }
    }
}
