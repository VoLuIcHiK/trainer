package ru.neuron.sportapp.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.neuron.sportapp.data.VideoRecordFileSource
import java.io.InputStream
import java.time.LocalDate
import ru.neuron.sportapp.data.VideoRecordModel
import ru.neuron.sportapp.data.VideoRecordRepository
import java.time.LocalTime
import java.util.Calendar
import java.util.logging.Logger

class HomeViewModel(
    private val videoRecordRepository: VideoRecordRepository = VideoRecordFileSource()
): ViewModel() {
    fun onVideoSelected(context: Context, videoSelected: InputStream) {
        viewModelScope.launch {
            val videoRecord = VideoRecordModel(
                filename = "video_${LocalDate.now()}_${LocalTime.now().toSecondOfDay()}.mp4"
            )
            videoRecordRepository.saveVideoRecord(videoRecord, videoSelected, context)
            Log.d("MYDEBUG", "Video saved into ${context.filesDir.canonicalPath}")
        }
    }
}