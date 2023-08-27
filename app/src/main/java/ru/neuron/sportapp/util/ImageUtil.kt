package ru.neuron.sportapp.util

import android.graphics.Bitmap

object ImageUtil {
    fun bitmapToFloatArray(bitmap: Bitmap) {
        val intValues = IntArray(bitmap.width * bitmap.height)
        val floatValues = FloatArray(bitmap.width * bitmap.height * 3)
        bitmap.getPixels(intValues, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        for (i in intValues.indices) {
            val value = intValues[i]
            floatValues[i * 3 + 0] = ((value shr 16 and 0xFF) - 128) / 128.0f
            floatValues[i * 3 + 1] = ((value shr 8 and 0xFF) - 128) / 128.0f
            floatValues[i * 3 + 2] = ((value and 0xFF) - 128) / 128.0f
        }
    }

    fun floatArrayToBitmap(data: FloatArray): Bitmap {
        val bitmap = Bitmap.createBitmap(data.size, 160, Bitmap.Config.ARGB_8888)
        val pixels = IntArray(160 * 160) // Set your expected output's height and width
        for (i in 0 until 160 * 160) {
            val a = 0xFF
            val r: Float = data[i * 3 + 0] * 255.0f
            val g: Float = data[i * 3 + 1] * 255.0f
            val b: Float = data[i * 3 + 2] * 255.0f
            pixels[i] = a shl 24 or (r.toInt() shl 16) or (g.toInt() shl 8) or b.toInt()
        }
        bitmap.setPixels(pixels, 0, 160, 0, 0, 160, 160)
        return bitmap
    }

    fun floatArrayNormalize(data: FloatArray): FloatArray {
        val newData = FloatArray(data.size)
        val max = data.max()
        for (i in data.indices) {
            newData[i] = data[i] / max
        }
        return newData
    }

}