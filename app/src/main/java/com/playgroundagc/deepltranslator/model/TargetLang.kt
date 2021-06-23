package com.playgroundagc.deepltranslator.model

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

    companion object {
        private val value = values().associateBy(TargetLang::language)
        operator fun get(source: String) = value[source]?.name
    }
}