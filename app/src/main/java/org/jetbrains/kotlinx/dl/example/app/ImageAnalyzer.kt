package org.jetbrains.kotlinx.dl.example.app

import android.content.Context
import android.content.res.Resources
import android.os.SystemClock
import androidx.camera.core.ImageProxy
import org.jetbrains.kotlinx.dl.api.inference.FlatShape
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel

internal class ImageAnalyzer(
    context: Context,
    private val resources: Resources,
    private val uiUpdateCallBack: (AnalysisResult?) -> Unit,
) {
    private val modelResourceId = resources.getIdentifier(
        "movenet161",
        "raw",
        context.packageName
    )
    private val inferenceModel = OnnxInferenceModel {
        resources.openRawResource(modelResourceId).use { it.readBytes() }
    }

    private val currentPipeline: InferencePipeline = PoseDetectionPipelineMy(
        MyModel(inferenceModel, "1234", "StatefulPartitionedCall:0")
    )

    fun analyze(image: ImageProxy, isImageFlipped: Boolean) {
        val pipeline = currentPipeline

        val start = SystemClock.uptimeMillis()
        val result = pipeline.analyze(image, confidenceThreshold)
        val end = SystemClock.uptimeMillis()

        val rotationDegrees = image.imageInfo.rotationDegrees
        image.close()

        if (result == null || result.confidence < confidenceThreshold) {
            uiUpdateCallBack(AnalysisResult.Empty(end - start))
        } else {
            uiUpdateCallBack(
                AnalysisResult.WithPrediction(
                    result, end - start,
                    ImageMetadata(image.width, image.height, isImageFlipped, rotationDegrees)
                )
            )
        }
    }

    fun close() {
        currentPipeline.close()
    }

    companion object {
        private const val confidenceThreshold = 0.5f
    }
}

sealed class AnalysisResult(val processTimeMs: Long) {
    class Empty(processTimeMs: Long) : AnalysisResult(processTimeMs)
    class WithPrediction(
        val prediction: Prediction,
        processTimeMs: Long,
        val metadata: ImageMetadata
    ) : AnalysisResult(processTimeMs)
}

interface Prediction {
    val shapes: List<FlatShape<*>>
    val confidence: Float
    fun getText(context: Context): String
}

data class ImageMetadata(
    val width: Int,
    val height: Int,
    val isImageFlipped: Boolean
) {

    constructor(width: Int, height: Int, isImageFlipped: Boolean, rotationDegrees: Int)
            : this(
        if (areDimensionSwitched(rotationDegrees)) height else width,
        if (areDimensionSwitched(rotationDegrees)) width else height,
        isImageFlipped
    )

    companion object {
        private fun areDimensionSwitched(rotationDegrees: Int): Boolean {
            return rotationDegrees == 90 || rotationDegrees == 270
        }
    }
}