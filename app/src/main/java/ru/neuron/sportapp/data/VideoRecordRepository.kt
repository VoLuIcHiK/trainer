package ru.neuron.sportapp.data

import android.content.Context
import java.io.InputStream


abstract class VideoRecordRepository(
) {
    abstract fun getVideoRecords(
        context: Context): List<VideoRecordModel>

    abstract fun getVideoRecord(videoRecordModel: VideoRecordModel,
                                context: Context): InputStream
    abstract fun saveVideoRecord(
        videoRecordModel: VideoRecordModel,
        inputStream: InputStream,
        context: Context
    )
    
    fun analyzeVideo(
        inputStream: InputStream
    ): AnalyzeVideoResult {
        return AnalyzeVideoResult(
            posesProbabilities = listOf(0.1f, 0.2f, 0.3f, 0.4f)
        )
    }

    open class AnalyzeVideoResult(
        val posesProbabilities: List<Float>
    )
    class NotFoundVideoRecordException: Exception()
}