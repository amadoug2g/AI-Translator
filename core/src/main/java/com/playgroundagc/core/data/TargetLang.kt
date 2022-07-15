package com.playgroundagc.core.data

/**
 * Created by Amadou on 23/06/2021, 14:48
 *
 * Target Language List
 *
 */

enum class TargetLang(val language: String) {
    BG("Bulgarian"),
    ZH("Chinese"),
    CS("Czech"),
    DA("Danish"),
    NL("Dutch"),
    EN("English"),
    EN_US("English (American)"),
    EN_GB("English (British)"),
    ET("Estonian"), //8
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
    PT_PT("Portuguese"), //19
    PT_BR("Portuguese (Brazil)"),
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
        private val value = values().associateBy(TargetLang::language)
        operator fun get(source: String) = value[source]?.name
    }
}