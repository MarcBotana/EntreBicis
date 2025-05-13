package cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.viewModels.RouteViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.CustomTopBar
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel

@Composable
fun RouteDetail(
    viewModel: RouteViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController,
    id: Long
) {
    val context = LocalContext.current

    val userData by sessionViewModel.userData.collectAsState()
    val routeDetail by viewModel.routeDetail.collectAsState()

    val routeNotFoundError by viewModel.routeNotFoundError.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(id) {
        viewModel.loadRouteDetail(id)
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
                    modifier = Modifier.fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.LightGray),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = routeDetail!!.id.toString()
                    )
                }
            }
        }
    }
}