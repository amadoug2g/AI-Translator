package com.playgroundagc.deepltranslator.common

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing

/**
 * Created by Amadou on 12/08/2021, 09:57
 *
 * Mockito Functions
 *
 */

inline fun <reified T> mock(): T = Mockito.mock(T::class.java)
inline fun <T> whenever(methodCall: T) : OngoingStubbing<T> = Mockito.`when`(methodCall)