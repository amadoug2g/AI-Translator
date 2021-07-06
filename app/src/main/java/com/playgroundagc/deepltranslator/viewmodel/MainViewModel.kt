package com.playgroundagc.deepltranslator.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playgroundagc.deepltranslator.model.TranslationText
import com.playgroundagc.deepltranslator.model.Translations
import com.playgroundagc.deepltranslator.repository.Repository
import kotlinx.coroutines.launch

/**
 * Created by Amadou on 20/06/2021, 19:35
 *
 * Main ViewModel Class
 *
 */

class MainViewModel(private val repository: Repository): ViewModel() {
    val translations: MutableLiveData<Translations> = MutableLiveData()
//    private val _response = MutableLiveData<Translations>()
//    val response: LiveData<Response>
//        get() = _response

    fun getTranslation(translationText: TranslationText) {
        viewModelScope.launch {
            val response = repository.getTranslation(translationText)
            translations.value = response
        }
    }

//    fun assignResponse(response: Translations?) {
//        _response.value = response
//    }
}