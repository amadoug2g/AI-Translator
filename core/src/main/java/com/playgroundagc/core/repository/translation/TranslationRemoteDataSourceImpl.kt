package com.playgroundagc.core.repository.translation

import com.playgroundagc.core.data.TranslationList
import com.playgroundagc.core.data.TranslationResponse
import com.playgroundagc.core.data.UsageResponse
import retrofit2.Response

/**
 * Created by Amadou on 17/07/2022, 19:28
 *
 * Purpose:
 *
 */

class TranslationRemoteDataSourceImpl(private val translationApi: TranslationApi) :
    TranslationApiService {
    override suspend fun translateText(
        auth_key: String,
        text: String,
        target_lang: String
    ): Response<TranslationList> {
        return translationApi.retrofitService.translateText(auth_key, text, target_lang)
    }

    override suspend fun getUsage(AUTH_KEY: String): Response<UsageResponse> {
        return translationApi.retrofitService.getUsage()
    }
}