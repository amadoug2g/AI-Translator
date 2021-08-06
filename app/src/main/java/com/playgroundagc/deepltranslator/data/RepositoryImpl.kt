package com.playgroundagc.deepltranslator.data

import com.playgroundagc.deepltranslator.domain.TranslationText
import com.playgroundagc.deepltranslator.domain.Translations
import com.playgroundagc.deepltranslator.util.Constants.AUTH_KEY
import retrofit2.Response

/**
 * Created by Amadou on 20/06/2021, 19:33
 *
 * Application Repository Implementation
 *
 */

class RepositoryImpl (private val localDataSource: LocalDataSource, private val remoteDataSource: RemoteDataSource) : Repository {
    override suspend fun getTranslation(translationText: TranslationText): Response<Translations> {
        return remoteDataSource.translateText(AUTH_KEY, translationText.text, translationText.target_lang)
    }
}