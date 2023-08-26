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

    class NotFoundVideoRecordException: Exception()
}