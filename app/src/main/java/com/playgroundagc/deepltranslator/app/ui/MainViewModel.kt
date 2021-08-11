package com.playgroundagc.deepltranslator.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.playgroundagc.deepltranslator.domain.*
import com.playgroundagc.deepltranslator.usecases.TranslateTextUseCase
import kotlinx.coroutines.launch
import java.net.InetAddress
import java.net.UnknownHostException
import java.util.concurrent.*
import retrofit2.Response as Resp

/**
 * Created by Amadou on 20/06/2021, 19:35
 *
 * Main ViewModel Class
 *
 */

class MainViewModel(private val translateTextUseCase: TranslateTextUseCase) : ViewModel() {

    //region Variables
    val translations: MutableLiveData<Translations> = MutableLiveData()

    private val _response = MutableLiveData<Response>()
    val response: LiveData<Response> = _response

    val textEntered = MutableLiveData<String>()

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean> = _loadingState

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private val _lastUsedSourceLang = MutableLiveData<Int>()
    val lastUsedSourceLang: LiveData<Int> = _lastUsedSourceLang

    private val _lastUsedTargetLang = MutableLiveData<Int>()
    val lastUsedTargetLang: LiveData<Int> = _lastUsedTargetLang
    //endregion

    //region Functions
    fun formatTranslation(text: String, sourceLang: SourceLang, targetLang: TargetLang) {
        _loadingState.postValue(true)
        try {
            when (sourceLang.language) {
                SourceLang.AUTO.language -> {
                    when (targetLang.language) {
                        TargetLang.EN_GB.language -> {
                            queryTranslation(
                                TranslationText(
                                    text,
                                    SourceLang[sourceLang.language]!!,
                                    "EN-GB"
                                )
                            )
                        }
                        TargetLang.EN_US.language -> {
                            queryTranslation(
                                TranslationText(
                                    text,
                                    SourceLang[sourceLang.language]!!,
                                    "EN-US"
                                )
                            )
                        }
                        else -> {
                            queryTranslation(
                                TranslationText(
                                    text,
                                    "",
                                    TargetLang[targetLang.language]!!
                                )
                            )
                        }
                    }
                }
                else -> {
                    when (targetLang.language) {
                        TargetLang.EN_GB.language -> {
                            queryTranslation(
                                TranslationText(
                                    text,
                                    SourceLang[sourceLang.language]!!,
                                    "EN-GB"
                                )
                            )
                        }
                        TargetLang.EN_US.language -> {
                            queryTranslation(
                                TranslationText(
                                    text,
                                    SourceLang[sourceLang.language]!!,
                                    "EN-US"
                                )
                            )
                        }
                        else -> {
                            queryTranslation(
                                TranslationText(
                                    text,
                                    SourceLang[sourceLang.language]!!,
                                    TargetLang[targetLang.language]!!
                                )
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            onResult(e.message.toString())
        }
    }

    private fun queryTranslation(translationText: TranslationText) {
        when (isOnline()) {
            true -> {
                getTranslation(translationText)
            }
            false -> {
                onResult("No Internet! Check your network")
            }
        }
    }

    private fun getTranslation(translationText: TranslationText) {
        viewModelScope.launch {
            val result = translateTextUseCase.invoke(translationText)

            when (result.isSuccessful) {
                true -> {
                    displayTranslation(result)
                }
                false -> {
                    handleError(result.code())
                }
            }
        }
    }

    private fun displayTranslation(result: Resp<Translations>) {
        translations.postValue(result.body())
        onResult()
    }

    private fun handleError(result: Int) {
        when (result) {
            400 -> {
                onResult("Bad request. Please check error message and your parameters.")
//                        onResult("Translation failed. Please try again.")
//                            onResult("$translationText")
            }
            403 -> {
                onResult("Authorization failed. Please supply a valid auth_key parameter.")
            }
            404 -> {
                onResult("The requested resource could not be found.")
            }
            413 -> {
                onResult("The request size exceeds the limit.")
            }
            414 -> {
                onResult("The request URL is too long. You can avoid this error by using a POST request instead of a GET request.")
            }
            429 -> {
                onResult("Too many requests. Please wait and resend your request.")
            }
            456 -> {
                onResult("Quota exceeded. The character limit has been reached.")
            }
            503 -> {
                onResult("Resource currently unavailable. Try again later.")
            }
            529 -> {
                onResult("Too many requests. Please wait and resend your request.")
            }
            else -> {
                onResult("Internal error")
            }
        }
    }

    private fun onResult(message: String = "") {
        _errorMessage.postValue(message)
        _loadingState.postValue(false)
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

    fun addSourceLang(position: Int) {
        _lastUsedSourceLang.postValue(position)
    }

    fun addTargetLang(position: Int) {
        _lastUsedTargetLang.postValue(position)
    }
    //endregion
}