package com.playgroundagc.deepltranslator.presentation.fragment

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playgroundagc.core.data.*
import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*

/**
 * Created by Amadou on 16/07/2022, 01:00
 *
 * Purpose: Fragments' ViewModel
 *
 */

//@HiltViewModel
class FragmentViewModel(
    private val translateTextUC: TranslateTextUC,
    private val getAPIUsageUC: GetAPIUsageUC
) : ViewModel() {

    //region Variables
    private val coroutineScope = CoroutineScope(IO)

    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()

    private val _apiUsage = MutableLiveData<UsageResponse>()
    val apiUsage: LiveData<UsageResponse> = _apiUsage

    private val _detectedLang = MutableLiveData<String>()
    val detectedLang: LiveData<String> = _detectedLang
    //endregion

    //region Setters
    init {
        apiUsageProcess()
    }

    fun setSourceLanguage(source: SourceLang, fromFunction: Boolean = true) {
        if (source.language == (_uiState.value.targetLang.language) && fromFunction) {
            switchLanguages()
        } else {
            _uiState.update { state -> state.copy(sourceLang = source) }
        }
    }

    fun setTargetLanguage(target: TargetLang, fromFunction: Boolean = true) {
        if (target.language == (_uiState.value.sourceLang.language) && fromFunction) {
            switchLanguages()
        } else {
            _uiState.update { state -> state.copy(targetLang = target) }
        }
    }

    fun switchLanguages() {
        val source = SourceLang.valueOf(_uiState.value.targetLang.name)
        val target = TargetLang.valueOf(_uiState.value.sourceLang.name)

        setTargetLanguage(target, false)
        setSourceLanguage(source, false)
    }

    fun setInputText(text: String) {
        _uiState.update { state -> state.copy(inputText = text) }
    }

    private fun setOutputText(response: TranslationResponse) {
        if (_uiState.value.sourceLang.name == "AUTO") {
            val result =
                "Detected language: ${getLanguage(response.detected_source_language.toString())} \n${response.text}"
            _uiState.update { state -> state.copy(outputText = result) }
        } else {
            _uiState.update { state -> state.copy(outputText = response.text.toString()) }
        }
    }

    private fun setLoadingStatus(status: Boolean) {
        _uiState.update { state -> state.copy(isFetchingTranslation = status) }
    }

    private fun setErrorMessage(message: String = "") {
        _uiState.update { state -> state.copy(errorMessage = message) }
        setLoadingStatus(false)
        _uiState.update { state -> state.copy(errorMessage = "") }
    }

    private fun setUsage(usage: UsageResponse?) {
        _apiUsage.postValue(usage)
        Log.d("Usage", "$usage")
    }

    private fun setDetectedLang(lang: String) {
        _detectedLang.postValue(lang)
    }
    //endregion

    //region Translation
    fun translationProcess() {
        setLoadingStatus(true)
        //if (isOnline()) {
        if (!_uiState.value.isInputEmpty()) {
            formatTranslation()
        } else {
            setErrorMessage("Translation input cannot be empty")
        }
        //} else {
        //    setErrorMessage("No Internet! Check your network")
        //}
    }

    private fun formatTranslation() {
        when (_uiState.value.targetLang.language) {
            TargetLang.EN_GB.language -> {
                coroutineScope.launch {
                    translateText(_uiState.value.toQuery("EN-GB"))
                }
            }
            TargetLang.EN_US.language -> {
                coroutineScope.launch {
                    translateText(_uiState.value.toQuery("EN-US"))
                }
            }
            else -> {
                coroutineScope.launch {
                    translateText()
                }
            }
        }
    }

    private suspend fun translateText(query: TranslationQuery = _uiState.value.toQuery()) {
        try {
            val response = translateTextUC.invoke(query)

            if (response.isSuccessful) {
                setOutputText(response.body()?.translations?.get(0) ?: TranslationResponse())
                setErrorMessage()
            } else {
                handleError(response.code())
            }
        } catch (e: Exception) {
            setErrorMessage(e.message.toString())
        }
    }
    //endregion

    //region API Usage
    private fun apiUsageProcess() {
        //setLoadingStatus(true)
        if (isOnline()) {
            coroutineScope.launch {
                getUsage()
            }
        } else {
            setErrorMessage("No Internet! Check your network")
        }
    }

    private suspend fun getUsage() {
        try {
            val result = getAPIUsageUC.invoke()

            if (result.isSuccessful) {
                setUsage(result.body())
                setErrorMessage()
            } else {
                setErrorMessage(result.message())
            }
        } catch (e: Exception) {
            setErrorMessage(e.message.toString())
        }
    }
    //endregion

    //region Functions
    private fun handleError(result: Int) {
        when (result) {
            400 -> {
                setErrorMessage("Bad request. Please check error message and your parameters.")
            }
            403 -> {
                setErrorMessage("Authorization failed. Please supply a valid auth_key parameter.")
            }
            404 -> {
                setErrorMessage("The requested resource could not be found.")
            }
            413 -> {
                setErrorMessage("The request size exceeds the limit.")
            }
            414 -> {
                setErrorMessage("The request URL is too long. You can avoid this error by using a POST request instead of a GET request.")
            }
            429 -> {
                setErrorMessage("Too many requests. Please wait and resend your request.")
            }
            456 -> {
                setErrorMessage("Quota exceeded. The character limit has been reached.")
            }
            503 -> {
                setErrorMessage("Resource currently unavailable. Try again later.")
            }
            529 -> {
                setErrorMessage("Too many requests. Please wait and resend your request.")
            }
            else -> {
                setErrorMessage("Internal error, please try again")
            }
        }
    }

    private fun isOnline(timeOut: Int = 1500): Boolean {
        var internetAddress: InetAddress? = null
        try {
            val future: Future<InetAddress?>? =
                Executors.newSingleThreadExecutor().submit(Callable<InetAddress?> {
                    try {
                        InetAddress.getByName("google.com")
                    } catch (e: UnknownHostException) {
                        null
                    }
                })
            internetAddress = future?.get(timeOut.toLong(), TimeUnit.MILLISECONDS)
            future?.cancel(true)
        } catch (e: InterruptedException) {
        } catch (e: ExecutionException) {
        } catch (e: TimeoutException) {
        }
        return internetAddress != null && !internetAddress.equals("")
    }

    fun isAutoSelected(): Boolean = _uiState.value.isAutoSelected()

    private fun getLanguage(response: String): String = SourceLang[response].toString()

    fun resetState() {
        _uiState.update { state ->
            state.copy(
                isFetchingTranslation = false,
                //isDetectingLanguage = true,
                //sourceLang = SourceLang.AUTO,
                //targetLang = TargetLang.BG,
                inputText = "",
                outputText = "",
                errorMessage = ""
            )
        }
    }
    //endregion
}
