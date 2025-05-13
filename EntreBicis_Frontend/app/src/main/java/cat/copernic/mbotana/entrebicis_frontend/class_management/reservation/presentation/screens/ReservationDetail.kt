package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.viewModels.ReservationViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.enums.ReservationState
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel

@Composable
fun ReservationDetail(
    viewModel: ReservationViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    id: Long
) {
    val context = LocalContext.current

    val navHostController = rememberNavController()

    val userData by sessionViewModel.userData.collectAsState()
    val reservationDetail by viewModel.reservationDetail.collectAsState()

    val reservationNotFoundError by viewModel.reservationNotFoundError.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadReservationDetail(id)
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
            modifier = Modifier.padding(paddingValues),
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
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${reservationDetail!!.id}-${reservationDetail!!.reservationCode}"
                    )

                    Image(
                        painter = painterResource(R.drawable.entrebicis_logo),
                        contentDescription = "EntreBicis_Logo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(16f / 12f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = reservationDetail!!.reward.name
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = reservationDetail!!.reward.description
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                        Text(
                            text = reservationDetail!!.reward.observation
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = reservationDetail!!.reward.rewardState.name
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    if (reservationDetail!!.reservationState == ReservationState.ASSIGNED) {
                        Button(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally),
                            onClick = {
                                userData?.let { it1 -> viewModel.returnReservation(it1.email, id) }
                            }) {
                            Text("Recollir")
                        }
                    }
                }
            }
        }
    }
}
