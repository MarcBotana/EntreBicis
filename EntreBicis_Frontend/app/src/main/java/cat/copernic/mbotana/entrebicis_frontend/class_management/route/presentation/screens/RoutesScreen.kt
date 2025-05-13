package cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.route.presentation.viewModels.RouteViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RoutesScreen(
    viewModel: RouteViewModel,
    sessionViewModel: SessionViewModel,
    navController: NavController
) {
    val context = LocalContext.current

    val userSession by sessionViewModel.userSession.collectAsState()

    val routeList by viewModel.routeList.collectAsState()
    val systemParams by viewModel.systemParams.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(viewModel) {
        viewModel.loadData(userSession.email)
    }

    LaunchedEffect(backendException) {
        backendException?.let { toastMessage(context, it) }
    }

    LaunchedEffect(frontendException) {
        frontendException?.let { toastMessage(context, it) }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {

            Spacer(modifier = Modifier.height(12.dp))
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {
                if (routeList?.isEmpty() == true) {
                    Text(
                        textAlign = TextAlign.Center,
                        text = "No tens rutes registrades."
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = routeList ?: emptyList()) { item ->
                            systemParams?.let { RouteItem(item, it, navController) }
                        }
                    }
                }
            }
        }
    }
}
