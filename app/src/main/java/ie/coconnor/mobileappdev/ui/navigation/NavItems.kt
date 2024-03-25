package ie.coconnor.mobileappdev.ui.navigation
import ie.coconnor.mobileappdev.R

sealed class BottomNavItem(
    var title: String,
    var icon: Int
) {
    object Home :
        BottomNavItem(
            "Home",
            R.drawable.red_hat_logo
        )

    object List :
        BottomNavItem(
            "List",
            R.drawable.red_hat_logo
        )

    object Analytics :
        BottomNavItem(
            "Analytics",
            R.drawable.red_hat_logo
        )

    object Profile :
        BottomNavItem(
            "AboutScreen",
            R.drawable.red_hat_logo
        )
}