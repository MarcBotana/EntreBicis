package cat.copernic.mbotana.entrebicis_frontend.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import cat.copernic.mbotana.entrebicis_frontend.R
import cat.copernic.mbotana.entrebicis_frontend.core.common.toastMessage
import kotlinx.coroutines.delay

@Composable
fun BottomNavigationBar(navController: NavController, isTrackingRoute: Boolean) {
    val context = LocalContext.current

    val items = listOf(
        BottomNavItem.Res,
        BottomNavItem.Rec,
        BottomNavItem.Map,
        BottomNavItem.Rou,
        BottomNavItem.Opt
    )

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    var showBlockedDialog by remember { mutableStateOf(false) }

    if (showBlockedDialog) {
        LaunchedEffect(true) {
            toastMessage(context, "Tens ua ruta activa!")
            delay(3500)
            showBlockedDialog = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .background(Color.White)
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 4.dp)
        ,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        items.forEach { item ->
            val isSelected = currentRoute == item.route
            val backgroundColor = if (isSelected) colorResource(R.color.appBlue) else Color.Transparent
            val contentColor = if (isSelected) Color.White else Color.Black

            val weight = if (item == BottomNavItem.Map) 1.8f else 1f

            val isMapItem = item == BottomNavItem.Map

            val iconAlpha = if (isTrackingRoute && !isMapItem) 0.4f else 1f

            Box(
                modifier = Modifier
                    .weight(weight)
                    .padding(horizontal = 2.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor)
                    .clickable {
                        if (currentRoute != item.route) {
                            if (isTrackingRoute && !isMapItem) {
                                showBlockedDialog = true
                            } else {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                    .padding(vertical = 8.dp, horizontal = 2.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = contentColor.copy(iconAlpha),
                        modifier = Modifier.size(22.dp)
                    )
                    Text(
                        text = item.label,
                        color = contentColor.copy(iconAlpha),
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}