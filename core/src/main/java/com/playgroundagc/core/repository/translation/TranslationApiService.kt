package com.playgroundagc.core.repository.translation

import com.playgroundagc.core.data.TranslationResponse
import com.playgroundagc.core.data.UsageResponse
import com.playgroundagc.core.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Amadou on 17/07/2022, 20:21
 *
 * Purpose: Api Service Interface
 *
 */

interface TranslationApiService {
    @GET("v2/translate")
    suspend fun translateText(
        @Query("auth_key") auth_key: String = Constants.AUTH_KEY,
        @Query("text") text: String,
        @Query("target_lang") target_lang: String
    ): Response<TranslationResponse>

    @GET("v2/usage")
    suspend fun getUsage(
        @Query("auth_key") AUTH_KEY: String = Constants.AUTH_KEY
    ): Response<UsageResponse>
}