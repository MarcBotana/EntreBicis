package cat.copernic.mbotana.entrebicis_frontend.class_management.reservation.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import cat.copernic.mbotana.entrebicis_frontend.core.common.ToastMessage
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

    val userSession by sessionViewModel.userSession.collectAsState()
    val rewardDetail by viewModel.rewardDetail.collectAsState()

    val rewardNotFoundError by viewModel.rewardNotFoundError.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadRewardDetail(id)
    }

    LaunchedEffect(backendException) {
        backendException?.let { ToastMessage(context, it) }
    }

    LaunchedEffect(frontendException) {
        frontendException?.let { ToastMessage(context, it) }
    }

    Scaffold(
        topBar =
        { CustomTopBar("Recompensa", userSession.totalPoints, true) },
    ) { paddingValues ->

        Surface(
            modifier = Modifier.padding(paddingValues),
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
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box {
                        Text(
                            text = rewardDetail!!.valuePoints.toString()
                        )
                    }

                    Column {
                        Image(
                            painter = painterResource(R.drawable.entrebicis_logo),
                            contentDescription = "EntreBicis_Logo",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(16f / 12f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = rewardDetail!!.name
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = rewardDetail!!.description
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row {
                        Text(
                            text = rewardDetail!!.observation
                        )
                        Text(
                            text = rewardDetail!!.rewardState.name
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally),
                        onClick = {
                                viewModel.makeReservation(userSession.email, id)
                        }) {
                        Text("Reservar")
                    }


                }
            }



        }
    }




}