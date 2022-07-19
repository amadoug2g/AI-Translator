package com.playgroundagc.core.data

/**
 * Created by Amadou on 15/07/2022, 20:27
 *
 * Purpose: Ui State Model
 *
 */

data class TranslationUiState(
    val isFetchingTranslation: Boolean = false,
    val isDetectingLanguage: Boolean = true,
    val sourceLang: SourceLang = SourceLang.AUTO,
    val targetLang: TargetLang = TargetLang.BG,
    val inputText: String = "",
    val outputText: String = "",
    val errorMessage: String = ""
) {
    fun toQuery(): TranslationQuery {
        return TranslationQuery(
            text = this.inputText,
            source_lang = sourceLang.name,
            target_lang = this.targetLang.name
        )
    }

    fun isInputEmpty(): Boolean = this.inputText.isEmpty()
}