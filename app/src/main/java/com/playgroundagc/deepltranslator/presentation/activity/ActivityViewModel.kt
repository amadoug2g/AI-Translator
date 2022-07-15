package com.playgroundagc.deepltranslator.presentation.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.playgroundagc.core.data.TranslationUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Created by Amadou on 15/07/2022, 20:21
 *
 * Purpose:
 *
 */

class ActivityViewModel(application: Application): AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()


}