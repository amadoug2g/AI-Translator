package com.playgroundagc.deepltranslator.presentation.fragment

import androidx.lifecycle.ViewModel
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.core.data.TargetLang
import com.playgroundagc.core.data.TranslationUiState
//import com.playgroundagc.deepltranslator.domain.SourceLang
//import com.playgroundagc.deepltranslator.domain.TargetLang
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Created by Amadou on 16/07/2022, 01:00
 *
 * Purpose: Fragments' ViewModel
 *
 */

class FragmentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()

    fun setSourceLanguage(source: SourceLang) {
        _uiState.update { state -> state.copy(sourceLang = source) }
    }x

    fun setTargetLanguage(target: TargetLang) {
        _uiState.update { state -> state.copy(targetLang = target) }
    }
}