package com.playgroundagc.deepltranslator.data

import com.playgroundagc.deepltranslator.domain.TranslationText
import com.playgroundagc.deepltranslator.domain.Translations
import retrofit2.Response

/**
 * Created by Amadou on 05/08/2021, 21:22
 *
 * Repository Interface
 *
 */

interface Repository {
    suspend fun getTranslation(translationText: TranslationText): Response<Translations>
}