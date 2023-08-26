package ru.neuron.sportapp.home

import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.opencv.core.Mat
import org.opencv.videoio.VideoCapture
import org.opencv.videoio.Videoio.CAP_ANY
import wseemann.media.FFmpegMediaMetadataRetriever
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.logging.Logger

class HomeViewModel: ViewModel() {
    fun onVideoSelected(videoSelected: InputStream, context: Context) {
        viewModelScope.launch {
            val logger = Logger.getGlobal()
            var tempFile = File.createTempFile("video", ".mp4")
            tempFile.deleteOnExit()
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(tempFile)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            if (videoSelected != null) {
                out?.write(videoSelected.read())
                videoSelected.close()
            }
            logger.info("read frame")

            val med = FFmpegMediaMetadataRetriever()
            med.setDataSource(tempFile.path)

//            out?.close()
//            val m = Mat()
//            val video = VideoCapture()
//            video.open(tempFile.path, CAP_ANY)
            val i = 0
            val millis = 100000
//            val ok = video.read(m)
//            logger.info("$ok")
            for (j in 1000000 until  millis*1000 step 1000000) {
                logger.info("read $i frame")
                val file = File(context.filesDir, "img$i.jpg")
                val bitmap = med.getFrameAtTime(
                    (j * 1000000).toLong(),
                    FFmpegMediaMetadataRetriever.OPTION_CLOSEST
                )
                val os: OutputStream = BufferedOutputStream(FileOutputStream(file))
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                os.close()
                logger.info("write $i frame")
            }
        }
    }
}