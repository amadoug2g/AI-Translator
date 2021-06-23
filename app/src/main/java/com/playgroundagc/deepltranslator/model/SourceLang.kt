package com.playgroundagc.deepltranslator.model

import java.lang.reflect.Type

/**
 * Created by Amadou on 23/06/2021, 14:45
 *
 * Source Language List
 *
 */

enum class SourceLang(val language: String) {
    AUTO("Any language"),
    BG("Bulgarian"),
    CS("Czech"),
    DA("Danish"),
    DE("German"),
    EL("Greek"),
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
    PT("Portuguese"),
    RO("Romanian"),
    RU("Russian"),
    SK("Slovak"),
    SL("Slovenian"),
    SV("Swedish"),
    ZH("Chinese");

    override fun toString() : String {
        return language
    }

    companion object {
        private val value = values().associateBy(SourceLang::language)
        operator fun get(source: String) = value[source]?.name
    }

//    companion object {
//        fun valueOf(value: String): Type = Type.values().find { it.value == value }
//    }

}