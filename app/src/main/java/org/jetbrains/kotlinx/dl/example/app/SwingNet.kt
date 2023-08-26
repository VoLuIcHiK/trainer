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
            for (j in 0 until 3 step 1) {
                buffer.put(i.slice(IntRange(j * 25600, (j + 1) * 25600 - 1)).toFloatArray())
            }/*from  w  w  w  . j  av a 2  s .c o  m*/
        }
        buffer.position(0)
        return buffer
    }

    fun directFloatBufferFromFloatArray1(data: FloatArray): FloatBuffer? {
        var buffer: FloatBuffer? = null
        val byteBuffer = ByteBuffer.allocateDirect(data.size * 4)
        byteBuffer.order(ByteOrder.BIG_ENDIAN)
        buffer = byteBuffer.asFloatBuffer()
        buffer.put(data) /*from  w  w  w  . j  av a 2  s .c o  m*/
        buffer.position(0)
        return buffer
    }

    private fun getOutputImage(output: FloatBuffer): Bitmap {
        output.rewind() // Rewind the output buffer after running.

        val bitmap = Bitmap.createBitmap(160, 160, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(160 * 160) // Set your expected output's height and width
        for (i in 0 until 160 * 160) {
            val a = 0xFF
            val r: Float = output.get() * 255.0f
            val g: Float = output.get() * 255.0f
            val b: Float = output.get() * 255.0f
            pixels[i] = a shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
        }
        bitmap.setPixels(pixels, 0, 160, 0, 0, 160, 160)

        return bitmap
    }

    fun inference(sourceArray: ArrayList<FloatArray>): Array<FloatArray> {
//        val buf1 = directFloatBufferFromFloatArray(arrayListOf(floatArrayOf(1.1F, 2.2F), floatArrayOf(3.3F, 4.4F)))
//        val buf2 = directFloatBufferFromFloatArray1(floatArrayOf(1.1F, 2.2F, 3.3F, 4.4F))
//        val arr2 = FloatArray(18432000 / 240 * 32)
//        arr2.fill(0.0F)
//        val newArr = FloatArray(sourceArray.size*3*160*160)
//        for (i in sourceArray){
//            newArr = conca
//        }
        val f_buff = directFloatBufferFromFloatArray1(sourceArray[0])
        val f_img = getOutputImage(f_buff!!)
        val arr4 = directFloatBufferFromFloatArray(sourceArray)
        val arr3 = longArrayOf(1, sourceArray.size.toLong(), 3, 160, 160)
        val tensorFromArray = OnnxTensor.createTensor(env, arr4, arr3)
//        val tensorFromArray1 = OnnxTensor.createTensor(env, FloatBuffer.wrap(sourceArray), arr3)

//        val tensorFromArray1 = reshape(tensorFromArray, )
        var t1: OnnxTensor = tensorFromArray
        val inputs = mapOf<String, OnnxTensor>("input_1" to t1)
        session.run(inputs).use {
                return it.get2DFloatArray("output_1")
        }
    }

    fun predict(bitmaps: ArrayList<Bitmap>): Array<FloatArray> {
        val array = ArrayList<FloatArray>()
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


