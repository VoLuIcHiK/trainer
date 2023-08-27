package ru.neuron.sportapp.home

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.neuron.sportapp.data.VideoRecordFileSource
import ru.neuron.sportapp.data.VideoRecordModel
import ru.neuron.sportapp.data.VideoRecordRepository
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream
import java.time.LocalDate
import java.time.LocalTime

class HomeViewModel(
    private val videoRecordRepository: VideoRecordRepository = VideoRecordFileSource()
): ViewModel() {
    fun onVideoSelected(context: Context, videoSelected: InputStream) {
        val videoRecord = VideoRecordModel(
            filename = "video_${LocalDate.now()}_${LocalTime.now().toSecondOfDay()}.mp4"
        )
//            Thread {
//                try {
//                    videoRecordRepository.saveVideoRecord(
//                        videoRecord, videoSelected, context
//                    )
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    Log.e("MYDEBUG", "Error: ${e.message} ${e.stackTraceToString()}")
//                }
//            }.start()
        viewModelScope.launch {
            try {
                val analyzeVideoResult = withContext(Dispatchers.Main) {
                    videoRecordRepository.analyzeVideo(videoSelected)
                    // run on ui thread
                }
                Toast.makeText(
                    context,
                    "Вероятности поз: ${analyzeVideoResult.posesProbabilities}",
                    Toast.LENGTH_SHORT
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("MYDEBUG", "Error: ${e.message} ${e.stackTraceToString()}")
            }
        }
    }

}