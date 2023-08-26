package ru.neuron.sportapp.home

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.kotlinx.dl.example.app.VideoProcessor
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
        viewModelScope.launch(Dispatchers.IO) {
            val videoRecord = VideoRecordModel(
                filename = "video_${LocalDate.now()}_${LocalTime.now().toSecondOfDay()}.mp4"
            )
            videoRecordRepository.saveVideoRecord(videoRecord, videoSelected, context)
            Log.d("MYDEBUG", "Video saved into ${context.filesDir.canonicalPath}")
            val videoFile = VideoRecordFileSource.videoRecordsFolder.resolve(videoRecord.filename)
            val videoProcessor: VideoProcessor = VideoProcessor(context, context.resources)
            Thread {
                val a = videoProcessor.analyze(videoFile)
            }.start()
        }

    }
}