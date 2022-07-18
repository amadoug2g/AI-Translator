package com.playgroundagc.deepltranslator.framework

import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC

/**
 * Created by Amadou on 18/07/2022, 10:08
 *
 * Purpose:
 *
 */

data class UseCases(
    val translateText: TranslateTextUC,
    val getAPIUsage: GetAPIUsageUC,
)
