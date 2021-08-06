package com.playgroundagc.deepltranslator.data

import com.playgroundagc.deepltranslator.app.framework.RetrofitInstance
import com.playgroundagc.deepltranslator.domain.Translations
import com.playgroundagc.deepltranslator.util.Constants
import retrofit2.Response

/**
 * Created by Amadou on 05/08/2021, 21:21
 *
 * Remote Data Source Implementation
 *
 */

class RemoteDataSourceImpl(private val retrofitInstance: RetrofitInstance) : RemoteDataSource {
    override suspend fun translateText(
        AUTH_KEY: String,
        text: String,
        target_lang: String
    ): Response<Translations> {
        return retrofitInstance.DeepLService.translateText(
            Constants.AUTH_KEY,
            text,
            target_lang
        )
    }
}