package ie.coconnor.mobileappdev.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null,
) {
    object LoginScreen : Destinations(
        route = "LoginScreen",
        title = "Login",
        icon = Icons.Outlined.Home
    )

    object LocationsScreen : Destinations(
        route = "LocationsScreen",
        title = "Explore",
        icon = Icons.Outlined.Home
    )

    object LocationDetailsScreen : Destinations(
        route = "LocationDetailsScreen",
        title = "LocationsDetails",
        icon = Icons.Outlined.Home
    )

    object PlanScreen : Destinations(
        route = "PlanScreen",
        title = "Plan",
        icon = Icons.Outlined.Favorite
    )

    object TestScreen : Destinations(
        route = "test_screen",
        title = "Test",
        icon = Icons.Outlined.AccessTime
    )

    object SettingsScreen : Destinations(
        route = "SettingsScreen",
        title = "Settings",
        icon = Icons.Outlined.Settings
    )

}

