package ru.neuron.sportapp.home

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.jetbrains.kotlinx.dl.example.app.R

@Composable
fun SportHome(homeViewModel: HomeViewModel) {
    val context = LocalContext.current
    val pickFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { videoUri ->
        if (videoUri != null) {
            context.contentResolver.openInputStream(videoUri)
                ?.let { homeViewModel.onVideoSelected(it, context) }
        }
    }
    Button(onClick = {
        pickFileLauncher.launch("video/*")
    }) {
        Text(stringResource(R.string.upload_video))
    }
}