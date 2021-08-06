package com.playgroundagc.deepltranslator.usecases

import com.playgroundagc.deepltranslator.data.Repository
import com.playgroundagc.deepltranslator.domain.TranslationText

/**
 * Created by Amadou on 05/08/2021, 22:58
 *
 * Translate Text UseCase
 *
 */

class TranslateTextUseCase(private val repository: Repository)  {
    suspend operator fun invoke(translationText: TranslationText) = repository.getTranslation(translationText)
}