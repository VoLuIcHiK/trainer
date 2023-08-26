package org.jetbrains.kotlinx.dl.example.app

import ai.onnxruntime.OnnxTensor
import ai.onnxruntime.OrtEnvironment
import ai.onnxruntime.OrtSession
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import org.jetbrains.kotlinx.dl.api.preprocessing.pipeline
import org.jetbrains.kotlinx.dl.impl.preprocessing.TensorLayout
import org.jetbrains.kotlinx.dl.impl.preprocessing.resize
import org.jetbrains.kotlinx.dl.impl.preprocessing.toFloatArray
import org.jetbrains.kotlinx.dl.onnx.inference.OrtSessionResultConversions.get2DFloatArray
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer


class SwingNet(
    context: Context,
    resources: Resources
){

    private var env: OrtEnvironment = OrtEnvironment.getEnvironment()
    private var session = loadModel(context, resources)

    fun directFloatBufferFromFloatArray(data: ArrayList<FloatArray>): FloatBuffer? {
        var buffer: FloatBuffer? = null
        val byteBuffer = ByteBuffer.allocateDirect(data.get(0).size * 4 * data.size)
//        byteBuffer.order(ByteOrder.nativeOrder())
        buffer = byteBuffer.asFloatBuffer()
        for (i in data){
            buffer.put(i) /*from  w  w  w  . j  av a 2  s .c o  m*/
        }
        buffer.position(0)
        return buffer
    }

    fun directFloatBufferFromFloatArray1(data: FloatArray): FloatBuffer? {
        var buffer: FloatBuffer? = null
        val byteBuffer = ByteBuffer.allocateDirect(data.size * 4)
//        byteBuffer.order(ByteOrder.nativeOrder())
        buffer = byteBuffer.asFloatBuffer()
        buffer.put(data) /*from  w  w  w  . j  av a 2  s .c o  m*/
        buffer.position(0)
        return buffer
    }

    fun inference(sourceArray: ArrayList<FloatArray>): Array<FloatArray> {
        val buf1 = directFloatBufferFromFloatArray(arrayListOf(floatArrayOf(1.1F, 2.2F), floatArrayOf(3.3F, 4.4F)))
        val buf2 = directFloatBufferFromFloatArray1(floatArrayOf(1.1F, 2.2F, 3.3F, 4.4F))
//        val arr2 = FloatArray(18432000 / 240 * 32)
//        arr2.fill(0.0F)
        val arr4 = directFloatBufferFromFloatArray(sourceArray)
        val arr3 = longArrayOf(1, sourceArray.size.toLong(), 3, 160, 160)
        val tensorFromArray = OnnxTensor.createTensor(env, arr4, arr3)
//        val tensorFromArray1 = reshape(tensorFromArray, )
        var t1: OnnxTensor = tensorFromArray
        val inputs = mapOf<String, OnnxTensor>("input_1" to t1)
        session.run(inputs).use {
                return it.get2DFloatArray("output_1")
        }
    }

    fun predict(bitmaps: ArrayList<Bitmap>): Array<FloatArray> {
        var array = ArrayList<FloatArray>()
        for (i in bitmaps){
            array.add(preprocessing.apply(i).first)
        }
        return inference(array)
    }

    fun close(){
        session.close()
    }

    private fun loadModel(context: Context, resources: Resources): OrtSession {
        val modelResourceId = resources.getIdentifier(
            "swingnet_norm",
            "raw",
            context.packageName
        )
        val inferenceModel = resources.openRawResource(modelResourceId).use { it.readBytes() }
        val options = OrtSession.SessionOptions()
        options.addCPU(true)
        return env.createSession(inferenceModel, OrtSession.SessionOptions())
    }


    val preprocessing = pipeline<Bitmap>()
        .resize {
            outputHeight = 160
            outputWidth = 160
        }
        .toFloatArray { layout = TensorLayout.NCHW }


}


