package ru.neuron.sportapp.data

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Environment
import android.util.Log
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Logger

class VideoRecordFileSource(
): VideoRecordRepository() {
    companion object {
        private const val videoRecordsFolderName = "video_records"
        private val downloadFolder: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS)
        val videoRecordsFolder = downloadFolder.resolve(videoRecordsFolderName)
    }

    override fun getVideoRecords(
        context: Context): List<VideoRecordModel> {
        val allFiles = mutableListOf<VideoRecordModel>()
        if (videoRecordsFolder.exists()) {
            videoRecordsFolder.listFiles()?.forEach {
                allFiles.add(VideoRecordModel(it.name))
            }
        }
        return allFiles.toList()
    }

    override fun getVideoRecord(videoRecordModel: VideoRecordModel,
                                context: Context): InputStream {
        val file = videoRecordsFolder.resolve(videoRecordModel.filename)
        if (!file.exists()) {
            throw VideoRecordRepository.NotFoundVideoRecordException()
        }
        return file.inputStream()
    }

    override fun saveVideoRecord(
        videoRecordModel: VideoRecordModel,
        inputStream: InputStream,
        context: Context
    ) {
        if (!videoRecordsFolder.exists()) {
            videoRecordsFolder.mkdir()
        }
        val file = videoRecordsFolder.resolve(videoRecordModel.filename)
        if (file.exists()) {
            file.delete()
        }
        file.createNewFile()
        file.outputStream().use {
            inputStream.copyTo(it)
        }
        Log.d("MYDEBUG", "saved video file: ${file.absoluteFile}")

        // Соханяем покадровое видео в отдельные кадры!
        val med = FFmpegMediaMetadataRetriever()
        Log.d("MYDEBUG","Init")
        med.setDataSource(file.path)
        Log.d("MYDEBUG","Opened")
        val value: String? = med.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_DURATION)
        var vidLength: Long = value!!.toLong() // it gives duration in seconds
        Log.d("MYDEBUG",vidLength.toString())

        var i = 0
        Log.d("MYDEBUG", context.filesDir.toString())
        for(j in 0 until  vidLength step 100) {
            Log.d("MYDEBUG","read $i frame")
            val file = File(videoRecordsFolder, "img$i.jpg")
            val bitmap = med.getFrameAtTime(
                j * 10000,
                FFmpegMediaMetadataRetriever.OPTION_CLOSEST
            )
            val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
            bitmap!!.compress(Bitmap.CompressFormat.PNG, 100, os);
            os.close()
            Log.d("MYDEBUG","write $i frame")
            i += 1
        }
        med.release()

    }
}