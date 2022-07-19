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
    fun toQuery(target: String = this.targetLang.name): TranslationQuery {
        return TranslationQuery(
            text = inputText,
            source_lang = sourceLang.name,
            target_lang = target
        )
    }

    fun isInputEmpty(): Boolean = inputText.isEmpty()

    fun isAutoSelected(): Boolean = sourceLang.name.equals(SourceLang.AUTO)
}