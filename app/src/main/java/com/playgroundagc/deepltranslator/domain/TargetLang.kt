package com.playgroundagc.deepltranslator.domain

import com.playgroundagc.deepltranslator.R

/**
 * Created by Amadou on 23/06/2021, 14:48
 *
 * Target Language List
 *
 */

enum class TargetLang(val language: String) {
    BG("Bulgarian"),
    CS("Czech"),
    DA("Danish"),
    DE("German"),
    EL("Greek"),
    EN_GB("English (British)"),
    EN_US("English (American)"),
    EN("English"),
    ES("Spanish"),
    ET("Estonian"),
    FI("Finnish"),
    FR("French"),
    HU("Hungarian"),
    IT("Italian"),
    JA("Japanese"),
    LT("Lithuanian"),
    LV("Latvian"),
    NL("Dutch"),
    PL("Polish"),
    PT_PT("Portuguese"),
    PT_BR("Portuguese (Brazil)"),
    RO("Romanian"),
    RU("Russian"),
    SK("Slovak"),
    SL("Slovenian"),
    SV("Swedish"),
    ZH("Chinese");

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
                "English (British)" -> result = R.drawable.english_british
                "English (American)" -> result = R.drawable.english_american
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
                "Portuguese (Brazil)" -> result = R.drawable.portuguese_brazil
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
        private val value = values().associateBy(TargetLang::language)
        operator fun get(source: String) = value[source]?.name
    }
}