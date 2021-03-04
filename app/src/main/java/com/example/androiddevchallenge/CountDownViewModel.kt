/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.launch

class CountDownViewModel : ViewModel() {

    var hours = mutableStateOf("00")
    var minutes = mutableStateOf("00")
    var seconds = mutableStateOf("00")

    private val _currentFormattedTimeLeft = mutableStateOf(BASE_TIME_FORMAT)
    val currentFormattedTimeLeft
        get() = _currentFormattedTimeLeft.value

    private val _countDownProgress = mutableStateOf(0f)
    val countDownProgress
        get() = _countDownProgress.value

    private val _state = mutableStateOf(State.IDLE)
    val state
        get() = _state.value

    private var tickerChannel: ReceiveChannel<Unit>? = null

    @ObsoleteCoroutinesApi
    fun startCountDown() {
        val amountOfSeconds = getCurrentlyEnteredTimeInSeconds()

        if (amountOfSeconds == 0) return

        _state.value = State.COUNTING_DOWN
        tickerChannel = ticker(delayMillis = 1_000)

        viewModelScope.launch {
            repeat(amountOfSeconds + 1) { secondsPassed ->
                _currentFormattedTimeLeft.value = (amountOfSeconds - secondsPassed).formatToTimeStr()
                _countDownProgress.value = secondsPassed.toFloat() / amountOfSeconds.toFloat()
                tickerChannel?.receive()
            }
            resetToIdleState()
        }
    }

    private fun getCurrentlyEnteredTimeInSeconds() = hours.value.toInt() * 60 * 60 + minutes.value.toInt() * 60 + seconds.value.toInt()

    private fun Int.formatToTimeStr() = let { totalSeconds ->
        val hours = totalSeconds / ONE_HOUR_IN_SECONDS
        val minutes = (totalSeconds % ONE_HOUR_IN_SECONDS) / ONE_MINUTE_IN_SECONDS
        val seconds = totalSeconds % ONE_MINUTE_IN_SECONDS

        String.format("%02dh %02dm %02ds", hours, minutes, seconds)
    }

    fun setCountdown() {
        _state.value = State.SET_COUNTDOWN
    }

    private fun resetToIdleState() {
        _countDownProgress.value = 0f
        _currentFormattedTimeLeft.value = BASE_TIME_FORMAT
        _state.value = State.IDLE
    }

    fun cancel() {
        tickerChannel?.cancel()
        resetToIdleState()
    }

    enum class State {
        COUNTING_DOWN,
        SET_COUNTDOWN,
        IDLE
    }

    private companion object {
        private const val ONE_HOUR_IN_SECONDS = 60 * 60
        private const val ONE_MINUTE_IN_SECONDS = 60
        private const val BASE_TIME_FORMAT = "00h 00m 00s"
    }
}
