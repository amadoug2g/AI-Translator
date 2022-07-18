package com.playgroundagc.deepltranslator.framework

import com.playgroundagc.deepltranslator.util.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Amadou on 17/07/2022, 20:50
 *
 * Purpose:
 *
 */

object TranslationApi {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val retrofitService: TranslationApiService by lazy {
        retrofit.create(TranslationApiService::class.java)
    }
}