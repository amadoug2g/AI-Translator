package com.playgroundagc.core.data

/**
 * Created by Amadou on 17/07/2022, 14:44
 *
 * Purpose: Request query to the API
 *
 */

data class TranslationQuery(
    var text: String = "",
    var source_lang: String? = null,
    var target_lang: TargetLang = TargetLang.EN,
    var split_sentences: String? = null,
    var preserve_formatting: String? = null,
    var formality: Formality? = Formality.DEFAULT,
    var glossary_id: String? = null,
)
