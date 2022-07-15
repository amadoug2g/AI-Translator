package com.playgroundagc.deepltranslator.presentation.fragment

import androidx.lifecycle.ViewModel
import com.playgroundagc.core.data.TranslationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Amadou on 16/07/2022, 01:00
 *
 * Purpose:
 *
 */
class FragmentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()


}