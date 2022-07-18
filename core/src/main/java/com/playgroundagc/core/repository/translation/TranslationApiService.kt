package com.playgroundagc.core.repository.translation

import com.playgroundagc.core.data.TranslationList
import com.playgroundagc.core.data.UsageResponse
import com.playgroundagc.core.util.Constants
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Amadou on 17/07/2022, 20:21
 *
 * Purpose: Api Service Interface
 *
 */

interface TranslationApiService {
    @POST("v2/translate")
    suspend fun translateText(
        @Query("auth_key") auth_key: String = Constants.AUTH_KEY,
        @Query("text") text: String,
        @Query("target_lang") target_lang: String
    ): Response<TranslationList>

    @POST("v2/usage")
    suspend fun getUsage(
        @Query("auth_key") AUTH_KEY: String = Constants.AUTH_KEY
    ): Response<UsageResponse>
}