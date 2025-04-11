package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState

@Composable
fun RewardItem(reward: Reward, navController: NavController) {
    Card(
        modifier = Modifier.padding(12.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.entrebicis_logo),
                contentDescription = "",
                modifier = Modifier.size(50.dp).weight(1f)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(5f)
            ) {
                Row {
                    Text(text = reward.name)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = reward.valuePoints.toString())
                }
                Text(text = reward.description)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",

            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun PreviewRewardItem() {

    val reward = Reward(
        1,
        "Name",
        "Description",
        "Observation",
        "",
        20.0,
        RewardState.AVAILABLE,
        null,
        null
    )

    val navController = rememberNavController()
    RewardItem(reward, navController)
}