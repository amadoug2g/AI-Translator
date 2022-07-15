package com.playgroundagc.deepltranslator.util

import com.playgroundagc.deepltranslator.R
import com.playgroundagc.deepltranslator.domain.SourceLang
import com.playgroundagc.deepltranslator.domain.TargetLang

/**
 * Created by Amadou on 15/07/2022, 18:49
 *
 * Purpose:
 *
 */

fun SourceLang.selectImageCategory(): Int {
    return when (this.language) {
        "Bulgarian" -> R.drawable.bulgarian
        "Czech" -> R.drawable.czech
        "Danish" -> R.drawable.danish
        "German" -> R.drawable.german
        "Greek" -> R.drawable.greek
        "English" -> R.drawable.english_british
        "Spanish" -> R.drawable.spanish
        "Estonian" -> R.drawable.estonian
        "Finnish" -> R.drawable.finnish
        "French" -> R.drawable.french
        "Hungarian" -> R.drawable.hungarian
        "Italian" -> R.drawable.italian
        "Japanese" -> R.drawable.japanese
        "Lithuanian" -> R.drawable.lithuanian
        "Latvian" -> R.drawable.latvian
        "Dutch" -> R.drawable.dutch
        "Polish" -> R.drawable.polish
        "Portuguese" -> R.drawable.portuguese
        "Romanian" -> R.drawable.romanian
        "Russian" -> R.drawable.russian
        "Slovak" -> R.drawable.slovak
        "Slovenian" -> R.drawable.slovenian
        "Swedish" -> R.drawable.swedish
        "Chinese" -> R.drawable.chinese
        else -> R.drawable.ic_question_mark
    }
}

fun TargetLang.selectImageCategory(): Int {
    return when (language) {
        "Bulgarian" -> R.drawable.bulgarian
        "Czech" -> R.drawable.czech
        "Danish" -> R.drawable.danish
        "German" -> R.drawable.german
        "Greek" -> R.drawable.greek
        "English" -> R.drawable.english_british
        "English (British)" -> R.drawable.english_british
        "English (American)" -> R.drawable.english_american
        "Spanish" -> R.drawable.spanish
        "Estonian" -> R.drawable.estonian
        "Finnish" -> R.drawable.finnish
        "French" -> R.drawable.french
        "Hungarian" -> R.drawable.hungarian
        "Italian" -> R.drawable.italian
        "Japanese" -> R.drawable.japanese
        "Lithuanian" -> R.drawable.lithuanian
        "Latvian" -> R.drawable.latvian
        "Dutch" -> R.drawable.dutch
        "Polish" -> R.drawable.polish
        "Portuguese" -> R.drawable.portuguese
        "Portuguese (Brazil)" -> R.drawable.portuguese_brazil
        "Romanian" -> R.drawable.romanian
        "Russian" -> R.drawable.russian
        "Slovak" -> R.drawable.slovak
        "Slovenian" -> R.drawable.slovenian
        "Swedish" -> R.drawable.swedish
        "Chinese" -> R.drawable.chinese
        else -> R.drawable.ic_question_mark
    }
}
