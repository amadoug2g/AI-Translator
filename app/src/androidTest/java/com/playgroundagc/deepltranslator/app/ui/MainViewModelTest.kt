package com.playgroundagc.deepltranslator.app.ui

import com.playgroundagc.deepltranslator.common.mock
import com.playgroundagc.deepltranslator.data.RepositoryImpl
import com.playgroundagc.deepltranslator.usecases.TranslateTextUseCase

/**
 * Created by Amadou on 12/08/2021, 09:52
 *
 *
 * View Model Test
 */

//@RunWith(AndroidJUnit4::class)
class MainViewModelTest {


    private val repository = mock<RepositoryImpl>()
    private val translateTextUseCase by lazy { TranslateTextUseCase(repository) }
//    private val viewModel = mock<MainViewModel>()
    private val viewModel by lazy { MainViewModel(translateTextUseCase) }

    

}