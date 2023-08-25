package ru.neuron.sportapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var startDestination = "login_screen"
        setContent {
            Text("")
            val navController = rememberNavController()
            val context = LocalContext.current
        }
    }
}
