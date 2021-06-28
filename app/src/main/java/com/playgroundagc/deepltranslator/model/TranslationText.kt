package com.playgroundagc.deepltranslator.model

/**
 * Created by Amadou on 20/06/2021, 23:26
 *
 * Translation Class File
 * : model class for translation text
 *
 */

data class TranslationText(
        var text: String,
        var source_lang: String,
        var target_lang: String
)