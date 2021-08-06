package com.playgroundagc.deepltranslator.domain

import com.google.gson.annotations.SerializedName

/**
 * Created by Amadou on 22/06/2021, 15:04
 *
 * Translations
 * : model file for translated response list
 *
 */

data class Translations(
        @SerializedName("translations")
        val translations: MutableList<Response>)