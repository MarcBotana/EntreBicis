package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.viewModels.RewardsViewModel
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage

@Composable
fun RewardsScreen(
    viewModel: RewardsViewModel,
    navController: NavController) {
    val context = LocalContext.current

    val search by viewModel.search.collectAsState()

    val rewardsList by viewModel.rewardsList.collectAsState()

    val backendException by viewModel.backendException.collectAsState()
    val frontendException by viewModel.frontendException.collectAsState()

    LaunchedEffect(key1 = viewModel) {
        viewModel.loadData()
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
            Row {
                OutlinedTextField(
                    value = search,
                    onValueChange = { viewModel.updateSearch(it) },
                    label = { Text(text = "Cercar...") },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp)
                        .height(46.dp)
                        .clip(RoundedCornerShape(50.dp))
                        .background(Color.White),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        cursorColor = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.LightGray)
            ) {
                if (rewardsList?.isEmpty() == true) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hi ha recompenses disponibles."
                        )
                    }

                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = rewardsList ?: emptyList()) { item ->

                            RewardItem(item, navController)
                        }
                    }
                }
            }
        }
    }
}