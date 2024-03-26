package ie.coconnor.mobileappdev.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Destinations(
    val route: String,
    val title: String? = null,
    val icon: ImageVector? = null
) {
    object LoginScreen : Destinations(
        route = "LoginScreen",
        title = "Login",
        icon = Icons.Outlined.Home
    )

//    object ExploreScreen : Destinations(
//        route = "ExploreScreen",
//        title = "Explore",
//        icon = Icons.Outlined.Home
//    )
    object TourScreen : Destinations(
        route = "TourScreen",
        title = "Tours",
        icon = Icons.Outlined.Home
    )

    object Favourite : Destinations(
        route = "favourite_screen",
        title = "Favorite",
        icon = Icons.Outlined.Favorite
    )

    object Notification : Destinations(
        route = "notification_screen",
        title = "Plan",
        icon = Icons.Outlined.Notifications
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

