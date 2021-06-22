package com.playgroundagc.deepltranslator.repository

import com.playgroundagc.deepltranslator.api.RetrofitInstance
import com.playgroundagc.deepltranslator.model.Translations
import com.playgroundagc.deepltranslator.model.TranslationText
import com.playgroundagc.deepltranslator.util.Constants.AUTH_KEY

/**
 * Created by Amadou on 20/06/2021, 19:33
 *
 * Application Repository
 *
 */

class Repository {
    suspend fun getTranslation(translationText: TranslationText): Translations {
        return RetrofitInstance.api.translateText(AUTH_KEY, translationText.text, translationText.target_lang)
    }
}