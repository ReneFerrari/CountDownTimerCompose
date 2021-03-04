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

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    val viewModel: CountDownViewModel = viewModel()

    Surface(color = MaterialTheme.colors.background) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .wrapContentSize()
                    .align(Alignment.CenterHorizontally)
            ) {
                if (viewModel.state == CountDownViewModel.State.SET_COUNTDOWN) {
                    SetCountDownTime(viewModel = viewModel)
                } else {
                    CountDownProgress(viewModel = viewModel)
                }
            }

            when (viewModel.state) {
                CountDownViewModel.State.COUNTING_DOWN -> CancelButton(viewModel = viewModel)
                CountDownViewModel.State.SET_COUNTDOWN,
                CountDownViewModel.State.IDLE -> SetUpButtons(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun ColumnScope.SetUpButtons(viewModel: CountDownViewModel) {
    Button(
        onClick = {
            viewModel.startCountDown()
        },
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Start Countdown",
            style = MaterialTheme.typography.button
        )
    }
    OutlinedButton(
        border = BorderStroke(1.dp, MaterialTheme.colors.primary),
        onClick = {
            viewModel.setCountdown()
        },
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 16.dp),

    ) {
        Text(
            text = "Set Countdown Time",
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun ColumnScope.CancelButton(viewModel: CountDownViewModel) {
    Button(
        onClick = {
            viewModel.cancel()
        },
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(top = 24.dp)
    ) {
        Text(
            text = "Cancel",
            style = MaterialTheme.typography.button
        )
    }
}

@Composable
fun BoxScope.CountDownProgress(viewModel: CountDownViewModel) {
    Text(
        text = viewModel.currentFormattedTimeLeft,
        style = MaterialTheme.typography.h5,
        modifier = Modifier.align(Alignment.Center)
    )
    CircularProgressIndicator(
        progress = viewModel.countDownProgress,
        strokeWidth = 6.dp,
        modifier = Modifier.size(220.dp)
    )
}

@Composable
fun SetCountDownTime(viewModel: CountDownViewModel) {
    var hours by viewModel.hours
    var minutes by viewModel.minutes
    var seconds by viewModel.seconds

    Column {
        TextField(
            value = hours,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                hours = it.take(2)
            },
            label = { Text("Hours") }
        )
        TextField(
            value = minutes,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                minutes = it.take(2)
            },
            label = { Text("Minutes") }
        )
        TextField(
            value = seconds,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = {
                seconds = it.take(2)
            },
            label = { Text("Seconds") }
        )
    }
}

@Preview("Light Theme", widthDp = 360, heightDp = 640)
@Composable
fun LightPreview() {
    MyTheme {
        MyApp()
    }
}

@Preview("Dark Theme", widthDp = 360, heightDp = 640)
@Composable
fun DarkPreview() {
    MyTheme(darkTheme = true) {
        MyApp()
    }
}
