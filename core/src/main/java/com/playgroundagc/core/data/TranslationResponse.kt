package com.playgroundagc.core.data

/**
 * Created by Amadou on 17/07/2022, 14:13
 *
 * Purpose: Response from the API
 *
 */

data class TranslationResponse(
    var detected_source_language: String? = null,
    var text: String? = null,
)
