package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.core.session.presentation.viewModel.SessionViewModel

@Composable
fun RewardsScreen(sessionViewModel: SessionViewModel, navController: NavController) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Transparent
    ) {
        Text(
            text = "Recompenses",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
        )
    }
}