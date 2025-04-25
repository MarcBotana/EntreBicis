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
import androidx.compose.material.icons.filled.AccessTimeFilled
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import cat.copernic.mbotana.entrebicis_frontend.core.enums.ReservationState
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationItem(reservation: Reservation, navController: NavController) {

    val expired = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("reservationDetail/${reservation.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.entrebicis_logo),
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

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
                            .padding(vertical = 8.dp)
                            .weight(1f)
                    ) {

                        val colorState = when (reservation.reservationState) {
                            ReservationState.PENDING -> Color(0xFFFFC107)
                            ReservationState.ACTIVE -> Color(0xFF2196F3)
                            ReservationState.CANCELED -> Color(0xFFF44336)
                            ReservationState.COMPLETED -> Color(0xFF4CAF50)
                        }

                        val iconState = when (reservation.reservationState) {
                            ReservationState.PENDING -> Icons.Default.AccessTime
                            ReservationState.ACTIVE -> Icons.Default.Beenhere
                            ReservationState.CANCELED -> Icons.Default.Cancel
                            ReservationState.COMPLETED -> Icons.Default.CheckCircle
                        }

                        Icon(
                            imageVector = iconState,
                            contentDescription = "",
                            tint = colorState
                        )
                        Text(
                            text = reservation.reservationState.name,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .padding(top = 2.dp, start = 3.dp),
                            color = colorState
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    val returnTime = LocalDateTime.parse(reservation.returnTime)

                    expired.value = returnTime.isBefore(LocalDateTime.now())

                    Text(
                        text = if (expired.value) "Caducat: ${parseReservationTime(returnTime)}" else "Caduca: ${parseReservationTime(returnTime)}" ,
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

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = Color.Gray
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun parseReservationTime(parsedDate: LocalDateTime): String {
    val formatterOutput = DateTimeFormatter.ofPattern("dd-MM-yyyy")
    return parsedDate.format(formatterOutput)
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewReservationItem() {

    val reward = Reward(
        1,
        "Name",
        "Description",
        "Observation",
        "",
        20.0,
        RewardState.AVAILABLE,
        null,
        null
    )

    val reservation = Reservation(
        id = 1,
        reservationCode = "84754G",
        reservationState = ReservationState.ACTIVE,
        reservationTime = "24-4-2025",
        returnTime = "27-4-2025 18:59",
        user = null,
        reward = reward
    )

    val navController = rememberNavController()
    ReservationItem(reservation, navController)
}