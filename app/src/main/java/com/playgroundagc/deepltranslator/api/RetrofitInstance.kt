package com.playgroundagc.deepltranslator.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.playgroundagc.deepltranslator.util.Constants.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Amadou on 20/06/2021, 19:31
 *
 * Retrofit Instance
 * : retrofit initialization file
 *
 */

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val api: SimpleApi by lazy {
        retrofit.create(SimpleApi::class.java)
    }
}