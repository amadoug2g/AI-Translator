package com.playgroundagc.deepltranslator.app.framework

import com.playgroundagc.deepltranslator.data.RemoteDataSource
import com.playgroundagc.deepltranslator.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by Amadou on 20/06/2021, 19:31
 *
 * Retrofit Instance initialization
 *
 */

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    val DeepLService: RemoteDataSource by lazy {
        retrofit.create(RemoteDataSource::class.java)
    }
}