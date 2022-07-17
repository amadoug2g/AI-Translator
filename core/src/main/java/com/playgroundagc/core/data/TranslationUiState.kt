package com.playgroundagc.core.data

/**
 * Created by Amadou on 15/07/2022, 20:27
 *
 * Purpose:
 *
 */

data class TranslationUiState(
    val isFetchingTranslation: Boolean = false,
    val isDetectingLanguage: Boolean = true,
    val sourceLang: SourceLang? = SourceLang.AUTO,
    val targetLang: TargetLang? = TargetLang.BG,
    val inputText: String? = "",
    val outputText: String? = "",
)
