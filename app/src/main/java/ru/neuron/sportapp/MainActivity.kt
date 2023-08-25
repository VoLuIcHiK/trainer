package ru.neuron.sportapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import org.jetbrains.kotlinx.dl.example.app.MainActivity
import ru.neuron.sportapp.ui.SportTheme

class MainActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SportTheme {

                val scaffoldState = rememberScaffoldState()
                ModalNavigationDrawer(
                    drawerContent = {
                        Surface(Modifier.fillMaxHeight().defaultMinSize(minWidth = 10.dp)) {
                            Text("Hi)", Modifier.padding(10.dp))
                        }

                    }
                ) {
                    NavHost(
                        modifier = Modifier,
                        navController = rememberNavController(),
                        startDestination = Routes.Home.route
                    ) {

                        composable(Routes.Home.route) {
                            Text("")
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartActivityForResult(),
                                onResult = { _ ->
                                }
                            )
                            Button(onClick = {
                                launcher.launch(Intent(applicationContext, MainActivity::class.java))
                            }) {
                                Text(text = "Start")
                            }
                        }
                    }
                }
            }
        }
    }
}
sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Training : Routes("training")
    object CameraTraining : Routes("camera_training")
}

