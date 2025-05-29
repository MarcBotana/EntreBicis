package cat.copernic.mbotana.entrebicis_frontend.core.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object RouteDateUtils {

    @RequiresApi(Build.VERSION_CODES.O)
    fun parseRouteDate(parsedDate: LocalDateTime): String {
        val formatterOutput = DateTimeFormatter.ofPattern("dd/MM/yy")
        return parsedDate.format(formatterOutput)
    }

}