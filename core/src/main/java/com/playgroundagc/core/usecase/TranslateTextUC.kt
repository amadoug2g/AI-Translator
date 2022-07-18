package com.playgroundagc.core.usecase

import com.playgroundagc.core.data.TranslationQuery
import com.playgroundagc.core.repository.Repository

/**
 * Created by Amadou on 15/07/2022, 18:10
 *
 * Purpose: Text Translation Use Case
 *
 */

class TranslateTextUC(private val repository: Repository) {
    suspend operator fun invoke(translationQuery: TranslationQuery) = repository.translateText(translationQuery)
}