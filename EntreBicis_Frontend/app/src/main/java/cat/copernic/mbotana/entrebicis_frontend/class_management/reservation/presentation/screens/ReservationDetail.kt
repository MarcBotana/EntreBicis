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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.viewModels.ReservationViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.common.ImageUtils
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.enums.ReservationState
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationDetail(
    viewModel: ReservationViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    id: Long
) {
    val context = LocalContext.current

    val userData by sessionViewModel.userData.collectAsState()
    val reservationDetail by viewModel.reservationDetail.collectAsState()

    val reservationNotFoundError by viewModel.reservationNotFoundError.collectAsState()

    val expired = remember { mutableStateOf(false) }


    val bitmap =
        remember(reservationDetail?.reward?.image) { ImageUtils.convertBase64ToBitmap(reservationDetail?.reward?.image) }

    val bitmapUser =
        remember(reservationDetail?.user?.image) { ImageUtils.convertBase64ToBitmap(reservationDetail?.user?.image) }

    val collectSuccess by viewModel.collectSuccess.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadReservationDetail(id)
    }

    LaunchedEffect(collectSuccess) {
        if (collectSuccess) {
            val bottomNavIndex = "Res"
            navController.navigate("main/$bottomNavIndex") {
                popUpTo(0) { inclusive = true }
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
        { userData?.let { CustomTopBar("Reserva", it, true) } },
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            reservationNotFoundError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            reservationDetail?.let {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFEFEFEF))
                        .padding(12.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Codi: ${reservationDetail!!.reservationCode}",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
                                .clickable {
                                    navController.navigate("rewardDetail/${reservationDetail!!.reward.id}") // <- cambia esto por tu ruta de navegación
                                }
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = if (bitmap != null) {
                                    BitmapPainter(bitmap.asImageBitmap())
                                } else {
                                    painterResource(id = R.drawable.entrebicis_logo)
                                },
                                contentDescription = "",
                                modifier = Modifier
                                    .size(140.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.LightGray),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(16.dp))
                                        .background(Color(0xFF444444))
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Text(
                                        text = "Cost: ${reservationDetail!!.reward.valuePoints} pts",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = reservationDetail!!.reward.name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Estat:",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = reservationDetail!!.reward.rewardState.display ,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.LightGray).apply { alpha(0.8f) }
                                .fillMaxWidth()
                                .padding(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "Informació Reserva:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 22.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                HorizontalDivider(
                                    modifier = Modifier.fillMaxWidth(),
                                    thickness = 2.dp,
                                    color = Color.DarkGray
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Data Reserva:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            val reservationDate = LocalDateTime.parse(reservationDetail!!.reservationDate)
                            Text(
                                text = parseReservationDetailTime(reservationDate),
                                fontSize = 18.sp
                            )


                            if (reservationDetail!!.assignationDate != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Data Assignació:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                val assignationDate = LocalDateTime.parse(reservationDetail!!.assignationDate)
                                Text(
                                    text = parseReservationDetailTime(assignationDate),
                                    fontSize = 18.sp
                                )
                            }

                            if (reservationDetail!!.returnDate != null) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Data Recollida:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                val returnDate = LocalDateTime.parse(reservationDetail!!.returnDate)
                                Text(
                                    text = parseReservationDetailTime(returnDate),
                                    fontSize = 18.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Punt recollida:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = reservationDetail!!.reward.exchangePoint,
                                fontSize = 18.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Reservat per:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White)
                                .padding(vertical = 6.dp, horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = if (bitmapUser != null) {
                                    BitmapPainter(bitmapUser.asImageBitmap())
                                } else {
                                    painterResource(id = R.drawable.user_avatar)
                                },
                                contentDescription = "User Image",
                                modifier = Modifier.size(62.dp)
                                    .clip(RoundedCornerShape(50.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            reservationDetail!!.user?.let { it1 ->
                                Text(
                                    text = it1.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                                Text(
                                    text = it1.surname,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp
                                )
                            }

                        }
                    }



                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        if (reservationDetail!!.returnTime != null) {

                            val returnTime = LocalDateTime.parse(reservationDetail!!.returnTime)

                            expired.value = returnTime.isBefore(LocalDateTime.now())

                            if (reservationDetail!!.reservationState != ReservationState.ASSIGNED) {
                                expired.value = false
                            }

                            Row (
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(if (expired.value) Color.Red.copy(alpha = 0.5f) else Color.White)
                                    .padding(12.dp),
                            ) {
                                Text(
                                    text = "Caducitat:  ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 20.sp,
                                )
                                Text(
                                    text = parseReservationDetailTime(returnTime),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        if (reservationDetail!!.reservationState == ReservationState.ASSIGNED) {
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(0.6f)
                                    .height(60.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    userData?.let { it1 -> viewModel.returnReservation(it1.email, id) }
                                }) {
                                Text(
                                    text = "Recollir",
                                    fontSize = 18.sp
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

@RequiresApi(Build.VERSION_CODES.O)
fun parseReservationDetailTime(parsedDate: LocalDateTime): String {
    val formatterOutput = DateTimeFormatter.ofPattern("dd/MM/yy")
    return parsedDate.format(formatterOutput)
}
