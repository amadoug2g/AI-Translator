package com.playgroundagc.core.repository

import com.playgroundagc.core.data.TranslationQuery
import com.playgroundagc.core.data.TranslationResponse
import com.playgroundagc.core.data.UsageResponse
import com.playgroundagc.core.repository.text.TextLocalDataSource
import com.playgroundagc.core.repository.translation.TranslationApiService
import com.playgroundagc.core.util.Constants
import retrofit2.Response

/**
 * Created by Amadou on 17/07/2022, 02:40
 *
 * Purpose: Repository Implementation
 *
 */

class RepositoryImpl(
    private val translationApiService: TranslationApiService,
    //private val textLocalDataSource: TextLocalDataSource
) : Repository {
    override suspend fun translateText(translationQuery: TranslationQuery): Response<TranslationResponse> {
        return translationApiService.translateText(
            Constants.AUTH_KEY,
            translationQuery.text,
            translationQuery.target_lang.name
        )
    }

    override suspend fun getUsage(): Response<UsageResponse> {
        return translationApiService.getUsage(Constants.AUTH_KEY)
    }
}