package com.giraffe.livecodigapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.giraffe.livecodigapp.scratch.SimpleScreen
import com.giraffe.livecodigapp.ui.theme.LiveCodigAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LiveCodigAppTheme {
                SimpleScreen()
            }
        }
    }
}
