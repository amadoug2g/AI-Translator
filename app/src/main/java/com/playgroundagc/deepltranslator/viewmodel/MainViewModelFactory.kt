package com.playgroundagc.deepltranslator.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.playgroundagc.deepltranslator.repository.Repository

/**
 * Created by Amadou on 20/06/2021, 19:36
 *
 * TODO: File Description
 *
 */

class MainViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }

}