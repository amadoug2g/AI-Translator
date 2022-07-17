package com.playgroundagc.deepltranslator.presentation.adapter

import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.core.data.TargetLang

// import com.playgroundagc.deepltranslator.domain.SourceLang

/**
 * Created by Amadou on 16/07/2022, 01:42
 *
 * Purpose:
 *
 */

interface LanguageSelect {
    fun onClick(sourceLang: SourceLang)
    fun onClick(targetLang: TargetLang)
}