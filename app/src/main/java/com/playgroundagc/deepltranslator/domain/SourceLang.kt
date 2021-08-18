package com.playgroundagc.deepltranslator.domain

import com.playgroundagc.deepltranslator.R

/**
 * Created by Amadou on 23/06/2021, 14:45
 *
 * Source Language List
 *
 */

enum class SourceLang(val language: String) {
    AUTO("Any language"),
    BG("Bulgarian"),
    ZH("Chinese"),
    CS("Czech"),
    DA("Danish"),
    NL("Dutch"),
    EN("English"),
    ET("Estonian"),
    FI("Finnish"),
    FR("French"),
    DE("German"),
    EL("Greek"),
    HU("Hungarian"),
    IT("Italian"),
    JA("Japanese"),
    LV("Latvian"),
    LT("Lithuanian"),
    PL("Polish"),
    PT("Portuguese"),
    RO("Romanian"),
    RU("Russian"),
    SK("Slovak"),
    SL("Slovenian"),
    ES("Spanish"),
    SV("Swedish");

    override fun toString() : String {
        return language
    }

    val imageCategory: Int
        get() {
            var result: Int = R.drawable.ic_account
            when (language) {
                "Bulgarian" -> result = R.drawable.bulgarian
                "Czech" -> result = R.drawable.czech
                "Danish" -> result = R.drawable.danish
                "German" -> result = R.drawable.german
                "Greek" -> result = R.drawable.greek
                "English" -> result = R.drawable.english_british
                "Spanish" -> result = R.drawable.spanish
                "Estonian" -> result = R.drawable.estonian
                "Finnish" -> result = R.drawable.finnish
                "French" -> result = R.drawable.french
                "Hungarian" -> result = R.drawable.hungarian
                "Italian" -> result = R.drawable.italian
                "Japanese" -> result = R.drawable.japanese
                "Lithuanian" -> result = R.drawable.lithuanian
                "Latvian" -> result = R.drawable.latvian
                "Dutch" -> result = R.drawable.dutch
                "Polish" -> result = R.drawable.polish
                "Portuguese" -> result = R.drawable.portuguese
                "Romanian" -> result = R.drawable.romanian
                "Russian" -> result = R.drawable.russian
                "Slovak" -> result = R.drawable.slovak
                "Slovenian" -> result = R.drawable.slovenian
                "Swedish" -> result = R.drawable.swedish
                "Chinese" -> result = R.drawable.chinese
            }
            return result
        }

    companion object {
        private val value = values().associateBy(SourceLang::language)
        operator fun get(source: String) = value[source]?.name
    }
}