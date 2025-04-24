package cat.copernic.mbotana.entrebicis_frontend.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BikeScooter
import androidx.compose.material.icons.filled.Discount
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
    data object Res : BottomNavItem("reservations", Icons.Default.Discount, "Reserves")
    data object Rec : BottomNavItem("rewards", Icons.Default.ShoppingCart, "Recompenses")
    data object Map : BottomNavItem("map", Icons.Default.LocationOn, "Mapa")
    data object Rou : BottomNavItem("routes", Icons.Default.BikeScooter, "Rutes")
    data object Opt : BottomNavItem("options", Icons.Default.Menu, "Opcions")
}