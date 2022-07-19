package com.playgroundagc.core.data

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

    companion object {
        private val value = values().associateBy(SourceLang::name)
        operator fun get(source: String) = value[source]?.language
    }
}