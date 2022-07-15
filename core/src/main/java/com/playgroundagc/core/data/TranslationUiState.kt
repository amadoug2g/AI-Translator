package com.playgroundagc.core.data

/**
 * Created by Amadou on 15/07/2022, 20:27
 *
 * Purpose:
 *
 */

data class TranslationUiState(
    val isFetchingTranslation: Boolean = false,
    val sourceLang: SourceLang? = null,
    val targetLang: TargetLang? = null,
    val inputText: String? = null,
    val outputText: String? = null,
)
