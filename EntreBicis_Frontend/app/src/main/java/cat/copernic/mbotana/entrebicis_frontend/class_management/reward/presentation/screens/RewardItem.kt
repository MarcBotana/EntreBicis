package cat.copernic.mbotana.entrebicis_frontend.class_management.reward.presentation.screens

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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.class_management.reward.domain.models.Reward
import cat.copernic.mbotana.entrebicis_frontend.core.common.ImageUtils
import cat.copernic.mbotana.entrebicis_frontend.core.enums.RewardState

@Composable
fun RewardItem(reward: Reward, navController: NavController) {

    val bitmap = remember(reward.image) { ImageUtils.convertBase64ToBitmap(reward.image) }

    Card(
        modifier = Modifier
            .padding(top = 12.dp, start = 12.dp, end = 12.dp)
            .fillMaxWidth()
            .clickable {
                navController.navigate("rewardDetail/${reward.id}")
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
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
                    .size(100.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = reward.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier
                            .weight(2f),
                        textAlign = TextAlign.Start,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    val pointsText = if ((reward.valuePoints % 1).toFloat() == 0f) {
                        "${reward.valuePoints.toInt()}"
                    } else {
                        "${reward.valuePoints}"
                    }

                    val pointsTextAppend = buildAnnotatedString {
                        append(pointsText)
                        withStyle(style = SpanStyle(fontSize = MaterialTheme.typography.titleSmall.fontSize)) {
                            append(" pts")
                        }
                    }
                    val pointsWeight = if (pointsTextAppend.length > 8) 2f else 1f

                    Box(
                        modifier = Modifier
                            .weight(pointsWeight),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = pointsTextAppend,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.End,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(colorResource(R.color.appBlue).copy(alpha = 0.8f))
                                .padding(horizontal = 6.dp),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = reward.description,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                        .padding(8.dp),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "",
                tint = Color.Gray
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
        null.toString(),
        null.toString(),
        null.toString(),
        null.toString(),
        null,
        null
    )

    val navController = rememberNavController()
    RewardItem(reward, navController)
}