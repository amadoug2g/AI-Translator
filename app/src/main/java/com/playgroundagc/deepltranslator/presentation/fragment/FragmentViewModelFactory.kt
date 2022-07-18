package com.playgroundagc.deepltranslator.presentation.fragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC

/**
 * Created by Amadou on 18/07/2022, 09:58
 *
 * Purpose:
 *
 */

class FragmentViewModelFactory(
    private val translateText: TranslateTextUC,
    private val getAPIUsage: GetAPIUsageUC
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            TranslateTextUC::class.java, GetAPIUsageUC::class.java
        ).newInstance(translateText, getAPIUsage)
    }
}