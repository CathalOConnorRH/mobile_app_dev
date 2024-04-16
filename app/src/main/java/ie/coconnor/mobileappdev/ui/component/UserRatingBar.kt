package ie.coconnor.mobileappdev.ui.component

import android.view.MotionEvent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ie.coconnor.mobileappdev.R

@Composable
fun UserRatingBar(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    ratingState: Float,
    ratingIconPainter: Painter = painterResource(id = R.drawable.vector),
    selectedColor: Color = Color(0xFFFFD700),
    unselectedColor: Color = Color(0xFFA2ADB1)
) {
    Row(modifier = modifier.wrapContentSize()) {
        for (value in 1..5) {
            StarIcon(
                size = size,
                painter = ratingIconPainter,
                ratingValue = value,
                ratingState = ratingState,
                selectedColor = selectedColor,
                unselectedColor = unselectedColor
            )
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun StarIcon(
    size: Dp,
    ratingState: Float,
    painter: Painter,
    ratingValue: Int,
    selectedColor: Color,
    unselectedColor: Color
) {
    val tint by animateColorAsState(
        targetValue = if (ratingValue <= ratingState) selectedColor else unselectedColor,
        label = ""
    )

    Icon(
        painter = painter,
        contentDescription = null,
        modifier = Modifier
            .size(size),
        tint = tint
    )
}

@Preview
@Composable
fun PreviewUserRatingBar() {
    val ratingState = remember { mutableIntStateOf(0) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        UserRatingBar(ratingState = 4.5f)
    }
}