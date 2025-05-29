package cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.domain.models.Route
import cat.copernic.mbotana.entrebicis_frontend.class_management.systemParams.domain.models.SystemParams
import cat.copernic.mbotana.entrebicis_frontend.core.common.RouteDateUtils
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RouteState
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RouteItem(route: Route, systemParams: SystemParams, navController: NavController) {

    val maxVelPassed = remember { mutableStateOf(false) }

    val colorState = when (route.routeState) {
        RouteState.PENDING -> Color(0xFFFFC107)
        RouteState.VALIDATED -> Color(0xFF4CAF50)
        RouteState.NOT_VALIDATED -> Color(0xFFF44336)
    }

    val iconState = when (route.routeState) {
        RouteState.PENDING -> Icons.Default.AccessTime
        RouteState.VALIDATED -> Icons.Default.CheckCircle
        RouteState.NOT_VALIDATED -> Icons.Default.Cancel
    }



    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("routeDetail/${route.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorState.copy(alpha = 0.8f)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .padding(top = 4.dp, bottom = 4.dp)
                ) {
                    Icon(
                        imageVector = iconState,
                        contentDescription = ""
                    )
                    Text(
                        text = route.routeState.display,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .padding(top = 2.dp, start = 3.dp)
                    )
                }

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
                    .padding(top = 6.dp, start = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(R.drawable.route),
                    contentDescription = "",
                    modifier = Modifier
                        .size(95.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                        .padding(8.dp)
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
                        val routeDate = LocalDateTime.parse(route.routeDate)

                        Text(
                            text = RouteDateUtils.parseRouteDate(routeDate),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier
                                .weight(2f),
                            textAlign = TextAlign.Start,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        val pointsTextAppend = buildAnnotatedString {
                            val pointsText = if ((route.totalRoutePoints % 1.0) == 0.0) {
                                "${route.totalRoutePoints.toInt()}"
                            } else {
                                String.format(Locale.US, "%.2f", route.totalRoutePoints)
                            }
                            append(pointsText)
                            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                append(" pts")
                            }
                        }

                        val pointsStateText = when (route.routeState) {
                            RouteState.PENDING -> "~ $pointsTextAppend"
                            RouteState.VALIDATED -> "+ $pointsTextAppend"
                            RouteState.NOT_VALIDATED -> " - $pointsTextAppend"
                        }


                        val pointsWeight = if (pointsStateText.length > 8) 2f else 1f

                        Box(
                            modifier = Modifier
                                .weight(pointsWeight),
                            contentAlignment = Alignment.CenterEnd
                        ) {
                            Text(
                                text = pointsStateText,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.End,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(colorState.copy(alpha = 0.8f))
                                    .padding(horizontal = 6.dp),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray.copy(alpha = 0.5f))
                            .padding(8.dp)
                    ) {

                        val distanceTextAppend = buildAnnotatedString {
                            if (route.totalRouteDistance >= 1.0) {
                                val routeKm =
                                    String.format(Locale.US, "%.1f", route.totalRouteDistance)
                                append(routeKm)
                                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                    append(" km")
                                }
                            } else {
                                val routeM = (route.totalRouteDistance * 1000).toInt().toString()
                                append(routeM)
                                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                    append(" m")
                                }
                            }
                        }

                        val timeTextAppend = buildAnnotatedString {
                            val splitTime = route.totalRouteTime?.split(":")
                            if (splitTime != null) {
                                if (splitTime.size == 3) {
                                    val hours = splitTime[0]
                                    val minutes = splitTime[1]
                                    val seconds = splitTime[2]

                                    if (hours != "00") {
                                        append("$hours:$minutes")
                                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                            append(" h")
                                        }
                                    } else if (minutes != "00") {
                                        append("$minutes:$seconds")
                                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                            append(" min")
                                        }
                                    } else {
                                        append("$minutes:$seconds")
                                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                            append(" seg")
                                        }
                                    }
                                }
                            }
                        }

                        val avgVelTextAppend = buildAnnotatedString {
                            val routeAvgVel =
                                String.format(Locale.US, "%.1f", route.avgRouteVelocity)
                            append("~ $routeAvgVel")
                            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                append(" km/h")
                            }
                        }

                        val maxVelTextAppend = buildAnnotatedString {
                            val routeMaxVel =
                                String.format(Locale.US, "%.1f", route.maxRouteVelocity)
                            append("↑ $routeMaxVel")
                            withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                append(" km/h")
                            }
                        }

                        maxVelPassed.value = route.maxRouteVelocity > systemParams.maxVelocity

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {

                            Text(
                                text = distanceTextAppend,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )


                            Text(
                                text = avgVelTextAppend,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = timeTextAppend,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = maxVelTextAppend,
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                maxLines = 2,
                                color = (if (maxVelPassed.value) Color(0xFFF44336) else Color.Black),
                                overflow = TextOverflow.Ellipsis
                            )
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


