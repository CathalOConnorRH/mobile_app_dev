package ie.coconnor.mobileappdev.ui.components

// CardList.kt
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ie.coconnor.mobileappdev.models.locations.Tour

@Composable
fun CardList(cards: List<Tour>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        items(cards) { card ->
            CardItem(card)
        }
    }
}

@Composable
fun CardItem(card: Tour) {
    Tour(
        modifier = Modifier.padding(8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = card.location_id, style = TextStyle(fontSize = 20.sp))
            Text(text = card.location_id, style = TextStyle(fontSize = 14.sp))
            // Add other UI elements as needed
        }
    }
}

fun Tour(modifier: Modifier, elevation: Dp, function: @Composable () -> Unit) {
    TODO("Not yet implemented")
}
