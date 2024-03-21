package ie.coconnor.mobileappdev

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

/**
 * A customizable [TopAppBar] for the application.
 *
 * @param modifier Modifier for styling or positioning the [TopAppBar].
 * @param currentScreen The title to be displayed on the app bar.
 * @param showBackButton Flag indicating whether the back navigation button should be shown.
 * @param onBackButtonClick Callback for the back navigation button click event.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomAppBar(
    modifier: Modifier = Modifier,
    currentScreen: String = "",
    showBackButton: Boolean = true,
    onBackButtonClick: () -> Unit
) {
    TopAppBar(
        title = { Text(currentScreen) },
        modifier = modifier,
        navigationIcon = {
            if (showBackButton) {
                // Show back navigation button if allowed
                IconButton(onClick = onBackButtonClick) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        tint = Color.Black,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

/**
 * A preview function for [CustomAppBar].
 */
@Preview
@Composable
fun PreviewCustomTopAppBar() {
    // Example usage with navigation controller
    CustomAppBar(
        currentScreen = "TopAppBar",
        showBackButton = true,
        onBackButtonClick = {  }
    )
}