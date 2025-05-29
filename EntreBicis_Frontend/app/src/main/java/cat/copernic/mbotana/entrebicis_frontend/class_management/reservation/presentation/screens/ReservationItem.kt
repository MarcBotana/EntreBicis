package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Beenhere
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.domain.models.Reservation
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.core.common.ImageUtils
import cat.copernic.mbotana.entrebicis_frontend.core.enums.ReservationState
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationItem(reservation: Reservation, navController: NavController) {

    val bitmap = remember(reservation.reward.image) { ImageUtils.convertBase64ToBitmap(reservation.reward.image) }

    val expired = remember { mutableStateOf(false) }

    val colorState = when (reservation.reservationState) {
        ReservationState.RESERVED -> Color(0xFFD4A017)
        ReservationState.ASSIGNED -> Color(0xFF2196F3)
        ReservationState.CANCELED -> Color(0xFFF44336)
        ReservationState.RETURNED -> Color(0xFF4CAF50)
    }

    val iconState = when (reservation.reservationState) {
        ReservationState.RESERVED -> Icons.Default.AccessTime
        ReservationState.ASSIGNED -> Icons.Default.Beenhere
        ReservationState.CANCELED -> Icons.Default.Cancel
        ReservationState.RETURNED -> Icons.Default.CheckCircle
    }

    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("reservationDetail/${reservation.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorState.copy(alpha = 0.8f))
                    ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Codi: ${reservation.reservationCode}",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                )
                Spacer(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(Color.Gray)
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = if (bitmap != null) {
                        BitmapPainter(bitmap.asImageBitmap())
                    } else {
                        painterResource(id = R.drawable.entrebicis_logo)
                    },                    contentDescription = "",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Spacer(
                    modifier = Modifier
                        .width(1.dp)
                        .height(80.dp)
                        .background(Color.Gray)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = reservation.reward.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .weight(2f),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val pointsText = if ((reservation.reward.valuePoints % 1).toFloat() == 0f) {
                            "${reservation.reward.valuePoints.toInt()}"
                        } else {
                            "${reservation.reward.valuePoints}"
                        }

                        val pointsTextAppend = buildAnnotatedString {
                            append(pointsText)
                            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                append(" pts")
                            }
                        }
                        val pointsWeight = if (pointsTextAppend.length > 8) 2f else 1f

                        Box(
                            modifier = Modifier
                                .weight(pointsWeight),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = pointsTextAppend,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(colorResource(R.color.appBlue).copy(alpha = 0.8f))
                                    .padding(horizontal = 6.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFFF5F5F5))
                                .padding(vertical = 8.dp, horizontal = 2.dp)
                                .weight(1f)
                        ) {
                            Icon(
                                imageVector = iconState,
                                contentDescription = "",
                                tint = colorState
                            )
                            Text(
                                text = reservation.reservationState.display,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier
                                    .padding(top = 2.dp, start = 3.dp),
                                color = colorState
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        if (reservation.returnTime != null) {
                            val returnTime = LocalDateTime.parse(reservation.returnTime)

                            expired.value = returnTime.isBefore(LocalDateTime.now())

                            if (reservation.reservationState != ReservationState.ASSIGNED) {
                                expired.value = false
                            }

                            if (reservation.returnDate != null) {
                                val returnDate = LocalDateTime.parse(reservation.returnDate)

                                Text(
                                    text = "Recollit: ${parseReservationTime(returnDate)}" ,
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(colorState.copy(alpha = 0.5f))
                                        .padding(8.dp)
                                        .weight(1f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            } else {
                                Text(
                                    text = if (expired.value) "Caducat: ${parseReservationTime(returnTime)}" else "Caduca: ${parseReservationTime(returnTime)}",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (expired.value) Color.Red.copy(alpha = 0.5f) else Color.White)
                                        .padding(8.dp)
                                        .weight(1f),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "",
                    tint = Color.Gray
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseReservationTime(parsedDate: LocalDateTime): String {
    val formatterOutput = DateTimeFormatter.ofPattern("dd/MM/yy")
    return parsedDate.format(formatterOutput)
}
