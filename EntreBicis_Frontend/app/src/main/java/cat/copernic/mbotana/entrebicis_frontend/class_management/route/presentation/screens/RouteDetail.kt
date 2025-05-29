package cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.screens

import android.os.Build
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.viewModels.RouteViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.common.RouteDateUtils
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.RoundCap
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RouteDetail(
    viewModel: RouteViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    id: Long
) {
    val context = LocalContext.current

    val coroutineScope = rememberCoroutineScope()

    val userData by sessionViewModel.userData.collectAsState()
    val routeDetail by viewModel.routeDetail.collectAsState()

    val pointsLatLng by viewModel.pointsLatLng.collectAsState()
    val bounds by viewModel.bounds.collectAsState()
    val startRoutePoint by viewModel.startRoutePoint.collectAsState()
    val endRoutePoint by viewModel.endRoutePoint.collectAsState()

    val systemParams by viewModel.systemParams.collectAsState()
    val maxVelPassed = remember { mutableStateOf(false) }

    val routeNotFoundError by viewModel.routeNotFoundError.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(id) {
        viewModel.loadRouteDetail(id)
    }

    LaunchedEffect(pointsLatLng) {
        if (pointsLatLng.isNotEmpty()) {
            bounds?.let { bounds ->
                val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, 125)
                cameraPositionState.move(cameraUpdate)
            }
        }
    }

    LaunchedEffect(backendException) {
        backendException?.let { toastMessage(context, it) }
    }

    LaunchedEffect(frontendException) {
        frontendException?.let { toastMessage(context, it) }
    }

    Scaffold(
        topBar =
        { userData?.let { CustomTopBar("Ruta", it, true) } },
    ) { paddingValues ->

        Surface(
            modifier = Modifier.padding(paddingValues),
            color = Color.Transparent
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                routeNotFoundError?.let {
                    Text(
                        text = it,
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }
                routeDetail?.let {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.LightGray.copy(alpha = 0.8f)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp))
                                .border(
                                    width = 1.dp,
                                    color = Color.Black,
                                    shape = RoundedCornerShape(16.dp)
                                )

                        ) {
                            GoogleMap(
                                modifier = Modifier.fillMaxSize(),
                                cameraPositionState = cameraPositionState,
                                properties = MapProperties(
                                    isMyLocationEnabled = false
                                ),
                                uiSettings = MapUiSettings(
                                    zoomControlsEnabled = true,
                                    myLocationButtonEnabled = false,
                                    compassEnabled = true,
                                    scrollGesturesEnabled = true,
                                    zoomGesturesEnabled = true,
                                    tiltGesturesEnabled = false
                                )
                            ) {
                                if (pointsLatLng.isNotEmpty()) {
                                    Polyline(
                                        points = pointsLatLng,
                                        color = Color.Blue,
                                        width = 12f,
                                        startCap = RoundCap(),
                                        endCap = RoundCap()
                                    )
                                }

                                if (startRoutePoint != null) {
                                    Marker(
                                        state = MarkerState(position = startRoutePoint!!),
                                        title = "Inici Ruta"
                                    )
                                }

                                if (endRoutePoint != null) {
                                    Marker(
                                        state = MarkerState(position = endRoutePoint!!),
                                        title = "Final Ruta"
                                    )
                                }
                            }
                            FloatingActionButton(
                                onClick = {
                                    if (pointsLatLng.isNotEmpty()) {
                                        bounds?.let { bounds ->
                                            val cameraUpdate =
                                                CameraUpdateFactory.newLatLngBounds(bounds, 125)
                                            coroutineScope.launch {
                                                cameraPositionState.animate(cameraUpdate)

                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(16.dp)
                            ) {
                                Icon(Icons.Default.MyLocation, contentDescription = "Centrar ruta")
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(12.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray)
                                .apply { alpha(0.5f) },
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val routeDate = LocalDateTime.parse(routeDetail!!.routeDate)

                            val distanceTextAppend = buildAnnotatedString {
                                if (routeDetail!!.totalRouteDistance >= 1.0) {
                                    val routeKm =
                                        String.format(
                                            Locale.US,
                                            "%.1f",
                                            routeDetail!!.totalRouteDistance
                                        )
                                    append(routeKm)
                                    withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                        append(" km")
                                    }
                                } else {
                                    val routeM =
                                        (routeDetail!!.totalRouteDistance * 1000).toInt().toString()
                                    append(routeM)
                                    withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                        append(" m")
                                    }
                                }
                            }

                            val timeTextAppend = buildAnnotatedString {
                                val splitTime = routeDetail!!.totalRouteTime?.split(":")
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
                                    String.format(Locale.US, "%.1f", routeDetail!!.avgRouteVelocity)
                                append("~ $routeAvgVel")
                                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                    append(" km/h")
                                }
                            }

                            val maxVelTextAppend = buildAnnotatedString {
                                val routeMaxVel =
                                    String.format(Locale.US, "%.1f", routeDetail!!.maxRouteVelocity)
                                append("↑ $routeMaxVel")
                                withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                                    append(" km/h")
                                }
                            }

                            maxVelPassed.value =
                                routeDetail!!.maxRouteVelocity > systemParams!!.maxVelocity

                            Text(
                                text = "Informació Ruta:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 2.dp,
                                color = Color.DarkGray
                            )

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp),
                                horizontalAlignment = Alignment.Start
                            ) {
                                Text(
                                    text = "Data: ${RouteDateUtils.parseRouteDate(routeDate)}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    modifier = Modifier.padding(top = 12.dp)
                                )

                                Text(
                                    text = "Distància: $distanceTextAppend",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Temps: $timeTextAppend",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Vel. Mitjana: $avgVelTextAppend",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(top = 12.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Vel. Màxima: $maxVelTextAppend",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    fontSize = 18.sp,
                                    modifier = Modifier.padding(top = 12.dp),
                                    color = (if (maxVelPassed.value) Color(0xFFF44336) else Color.Black),
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFFE0E0E0))
                                    .padding(24.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Punts Generats: ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                                Text(
                                    text = "+ ${"%.2f".format(routeDetail!!.totalRoutePoints)} pts",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                FloatingActionButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp),
                    containerColor = Color(0xFF2196F3)
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Tornar enrere",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

