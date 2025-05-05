package cat.copernic.mbotana.entrebicis_frontend.core.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.core.session.model.SessionUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(screen: String, user: SessionUser, showPoints: Boolean) {

    val bitmap = remember(user.image) { ImageUtils.convertBase64ToBitmap(user.image) }

    TopAppBar(
        modifier = Modifier
            .drawBehind {
                val strokeWidth = 2.dp.toPx()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, size.height),
                    end = Offset(size.width, size.height),
                    strokeWidth = strokeWidth
                )
            },
        title = {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = if (bitmap != null) {
                            BitmapPainter(bitmap.asImageBitmap())
                        } else {
                            painterResource(id = R.drawable.entrebicis_logo)
                        },
                        contentDescription = "User Image",
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(screen, fontWeight = FontWeight.Bold)
                }

                if (showPoints){
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFDAD0D0), RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("${"%.2f".format(user.totalPoints)} pts")
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
    )
}
