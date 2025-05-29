package cat.copernic.mbotana.entrebicis_frontend.core.common

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

object ImageUtils {

    fun convertBase64ToBitmap(base64String: String?): Bitmap? {
        return try {
            if (!base64String.isNullOrEmpty()) {
                val byteArray = Base64.decode(base64String, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            } else {
                null
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }


}