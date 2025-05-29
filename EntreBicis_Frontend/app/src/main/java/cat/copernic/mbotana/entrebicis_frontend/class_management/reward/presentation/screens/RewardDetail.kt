package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.screens.parseReservationTime
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewModels.RewardsViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.common.ImageUtils
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RewardDetail(
    viewModel: RewardsViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    id: Long
) {
    val context = LocalContext.current

    val userData by sessionViewModel.userData.collectAsState()
    val rewardDetail by viewModel.rewardDetail.collectAsState()

    var showConfirmReservationDialog by remember { mutableStateOf(false) }

    val bitmap =
        remember(rewardDetail?.image) { ImageUtils.convertBase64ToBitmap(rewardDetail?.image) }

    val rewardNotFoundError by viewModel.rewardNotFoundError.collectAsState()

    val reservationSuccess by viewModel.reservationSuccess.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadRewardDetail(id)
    }

    LaunchedEffect(reservationSuccess) {
        if (reservationSuccess) {
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
        { userData?.let { CustomTopBar("Recompensa", it, true) } },
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            color = Color.Transparent
        ) {
            rewardNotFoundError?.let {
                Text(
                    text = it,
                    color = Color.Red,
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            rewardDetail?.let {
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
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White)
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
                                        text = "Cost: ${rewardDetail!!.valuePoints} pts",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = rewardDetail!!.name,
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
                                    text = rewardDetail!!.rewardState.display ,
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
                                    text = "Informació:",
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
                                text = "Descripció:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = rewardDetail!!.description,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Observacións:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = rewardDetail!!.observation,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Punt recollida:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                            Text(
                                text = rewardDetail!!.exchangePoint,
                                fontSize = 18.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Data creació:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )

                            val creationDate = LocalDateTime.parse(rewardDetail!!.rewardDate)
                            Text(
                                text = parseRewardTime(creationDate),
                                fontSize = 18.sp
                            )
                        }
                    }

                    if (rewardDetail!!.rewardState == RewardState.AVAILABLE) {
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            modifier = Modifier
                                .fillMaxWidth(0.6f)
                                .height(60.dp)
                                .align(Alignment.CenterHorizontally),
                            onClick = {
                                showConfirmReservationDialog = true
                            }
                        ) {
                            Text(
                                text = "Reservar",
                                fontSize = 18.sp
                            )
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

    if (showConfirmReservationDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmReservationDialog = false },
            text = {
                Text("Realitzar Reserva?\n\nCost: ${rewardDetail?.valuePoints} pts")
            },
            confirmButton = {
                TextButton(onClick = {
                    userData?.let { viewModel.makeReservation(it.email, id) }
                    showConfirmReservationDialog = false
                }) {
                    Text("Sí, reservar")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmReservationDialog = false
                }) {
                    Text("No")
                }
            }
        )
    }
}



@RequiresApi(Build.VERSION_CODES.O)
fun parseRewardTime(parsedDate: LocalDateTime): String {
    val formatterOutput = DateTimeFormatter.ofPattern("dd/MM/yy")
    return parsedDate.format(formatterOutput)
}

