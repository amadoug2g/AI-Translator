package com.playgroundagc.deepltranslator.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by Amadou on 22/06/2021, 14:45
 *
 * Response Class File
 * : model class for translated text
 *
 */

data class Response(
        @SerializedName("detected_source_language")
        var detected_source_language: String? = null,
        @SerializedName("text")
        var text: String? = null,
)