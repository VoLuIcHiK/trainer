package org.jetbrains.kotlinx.dl.example.app

import ai.onnxruntime.OrtSession
import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import org.jetbrains.kotlinx.dl.api.core.shape.TensorShape
import org.jetbrains.kotlinx.dl.api.inference.InferenceModel
import org.jetbrains.kotlinx.dl.api.inference.posedetection.DetectedPose
import org.jetbrains.kotlinx.dl.api.inference.posedetection.PoseEdge
import org.jetbrains.kotlinx.dl.api.inference.posedetection.PoseLandmark
import org.jetbrains.kotlinx.dl.api.preprocessing.Operation
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.TensorLayout
import org.jetbrains.kotlinx.dl.impl.preprocessing.camerax.toBitmap
import org.jetbrains.kotlinx.dl.impl.preprocessing.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.rotate
import org.jetbrains.kotlinx.dl.impl.preprocessing.toFloatArray
import org.jetbrains.kotlinx.dl.onnx.inference.CameraXCompatibleModel
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxHighLevelModel
import org.jetbrains.kotlinx.dl.onnx.inference.OnnxInferenceModel
import org.jetbrains.kotlinx.dl.onnx.inference.OrtSessionResultConversions.get2DFloatArray
import org.jetbrains.kotlinx.dl.onnx.inference.doWithRotation
import org.jetbrains.kotlinx.multik.api.ndarray

class SwingNet(
    override val internalModel: OnnxInferenceModel,
    override val modelKindDescription: String? = null,
    protected val outputName: String
) : OnnxHighLevelModel<Bitmap, FloatArray>,
    CameraXCompatibleModel {

    override var targetRotation: Int = 0

    override val preprocessing: Operation<Bitmap, Pair<FloatArray, TensorShape>>
        get() = pipeline<Bitmap>()
            .resize {
                outputHeight = internalModel.inputDimensions[0].toInt()
                outputWidth = internalModel.inputDimensions[1].toInt()
            }
            .rotate { degrees = targetRotation.toFloat() }
            .toFloatArray { layout = TensorLayout.NHWC }

    override fun close() {
        TODO("Not yet implemented")
    }

    override fun convert(output: OrtSession.Result): FloatArray {
        TODO("Not yet implemented")
    }

    fun predict(input: ArrayList<Bitmap>): FloatArray {
        var array = ArrayList<FloatArray>()
        for (i in input){
            array.add(preprocessing.apply(i).first)
        }
        return super.predict(input)
    }
}
