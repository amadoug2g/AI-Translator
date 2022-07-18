package com.playgroundagc.core.repository

import com.playgroundagc.core.data.TranslationQuery
import com.playgroundagc.core.data.TranslationResponse
import com.playgroundagc.core.data.UsageResponse
import retrofit2.Response

/**
 * Created by Amadou on 17/07/2022, 21:15
 *
 * Purpose: Repository Interface
 *
 */

interface Repository {
    suspend fun translateText(translationQuery: TranslationQuery): Response<TranslationResponse>
    suspend fun getUsage(): Response<UsageResponse>
}