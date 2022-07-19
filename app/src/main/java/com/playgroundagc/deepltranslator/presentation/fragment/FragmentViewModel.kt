package com.playgroundagc.deepltranslator.presentation.fragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.playgroundagc.core.data.SourceLang
import com.playgroundagc.core.data.TargetLang
import com.playgroundagc.core.data.TranslationUiState
import com.playgroundagc.core.data.UsageResponse
import com.playgroundagc.core.usecase.GetAPIUsageUC
import com.playgroundagc.core.usecase.TranslateTextUC
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*

/**
 * Created by Amadou on 16/07/2022, 01:00
 *
 * Purpose: Fragments' ViewModel
 *
 */

class FragmentViewModel(
    private val translateText: TranslateTextUC,
    private val getAPIUsage: GetAPIUsageUC
) : ViewModel() {

    //region Variables
    private val coroutineScope = CoroutineScope(IO)

    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()

    private val _apiUsage = MutableLiveData<UsageResponse>()
    val apiUsage: LiveData<UsageResponse> = _apiUsage

    val isInputEmpty = _uiState.value.isInputEmpty()
    //endregion

    //region Setters
    fun setSourceLanguage(source: SourceLang) {
        _uiState.update { state -> state.copy(sourceLang = source) }
    }

    fun setTargetLanguage(target: TargetLang) {
        _uiState.update { state -> state.copy(targetLang = target) }
    }

    fun setInputText(text: String) {
        _uiState.update { state -> state.copy(inputText = text) }
    }

    private fun setOutputText(text: String) {
        _uiState.update { state -> state.copy(outputText = text) }
    }

    private fun setLoadingStatus(status: Boolean) {
        _uiState.update { state -> state.copy(isFetchingTranslation = status) }
    }

    private fun setErrorMessage(message: String = "") {
        _uiState.update { state -> state.copy(errorMessage = message) }
        setLoadingStatus(false)
    }

    private fun setUsage(usage: UsageResponse?) {
        _apiUsage.postValue(usage)
    }
    //endregion

    //region Translation
    fun translationProcess() {
        setLoadingStatus(true)
        if (isOnline()) {
            coroutineScope.launch {
                translation()
            }
        } else {
            setErrorMessage("No Internet! Check your network")
        }
    }

    suspend fun translation() {
        try {
            val query = _uiState.value.toQuery()

            val response = translateText.invoke(query)

            if (response.isSuccessful) {
                setOutputText(response.body()?.translations?.get(0)?.text.toString())
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
    fun apiUsageProcess() {
        setLoadingStatus(true)
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
            val result = getAPIUsage.invoke()

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

    fun resetState() {
        _uiState.update { state ->
            state.copy(
                isFetchingTranslation = false,
                isDetectingLanguage = true,
                sourceLang = SourceLang.AUTO,
                targetLang = TargetLang.BG,
                inputText = "",
                outputText = "",
                errorMessage = ""
            )
        }
    }
    //endregion
}
