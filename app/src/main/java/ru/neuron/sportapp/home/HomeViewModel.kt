package ru.neuron.sportapp.home

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.neuron.sportapp.data.VideoRecordFileSource
import ru.neuron.sportapp.data.VideoRecordModel
import ru.neuron.sportapp.data.VideoRecordRepository
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
        Thread {
            videoRecordRepository.saveVideoRecord(videoRecord, videoSelected, context)
            videoSelected.reset()
            val analyzeVideoResult = videoRecordRepository.analyzeVideo(videoSelected)
            videoSelected.close()
            Toast.makeText(context, "Вероятности поз: ${analyzeVideoResult.posesProbabilities}", Toast.LENGTH_SHORT).show()
        }.start()
    }

}