package com.playgroundagc.deepltranslator.data

import com.playgroundagc.deepltranslator.domain.Translations
import com.playgroundagc.deepltranslator.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Amadou on 20/06/2021, 19:19
 *
 * Remote Data Source Interface
 *
 */

interface RemoteDataSource {
    @GET("v2/translate")
    suspend fun translateText(
        @Query("auth_key") AUTH_KEY: String = Constants.AUTH_KEY,
        @Query("text") text: String,
        @Query("target_lang") target_lang: String
    ): Response<Translations>
}