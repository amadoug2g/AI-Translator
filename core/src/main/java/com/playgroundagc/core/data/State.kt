package com.playgroundagc.core.data

/**
 * Created by Amadou on 18/07/2022, 22:22
 *
 * Purpose: tracking request status
 *
 */

sealed class State<T> {
    class Loading<T> : State<T>()
    data class Success<T>(val data: T) : State<T>()
    data class Failed<T>(val message: String) : State<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T) = Success(data)
        fun <T> failed(message: String) = Failed<T>(message)
    }
}