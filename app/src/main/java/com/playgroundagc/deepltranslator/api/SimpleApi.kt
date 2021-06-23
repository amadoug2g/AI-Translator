package com.playgroundagc.deepltranslator.api

import com.playgroundagc.deepltranslator.model.Translations
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Amadou on 20/06/2021, 19:19
 *
 * Api Interface
 *
 */

interface SimpleApi {

    @GET("v2/translate")
    suspend fun translateText(
            @Query("auth_key") AUTH_KEY: String,
            @Query("text") text: String,
            @Query("target_lang") target_lang: String
    ): Translations
}