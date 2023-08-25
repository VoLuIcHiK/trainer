package ru.neuron.sportapp.home

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.time.LocalDate
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_videoio.VideoCapture

class HomeViewModel: ViewModel() {
    fun onVideoSelected(videoSelected: InputStream) {
        viewModelScope.launch {

        }
    }

}