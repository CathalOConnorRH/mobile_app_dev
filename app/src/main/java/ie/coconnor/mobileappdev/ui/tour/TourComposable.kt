package composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import ie.coconnor.mobileappdev.ui.login.LoginScreenOld
import ie.coconnor.mobileappdev.R

@Composable
fun TourComposable() {
    Card(
        modifier = Modifier.padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        //elevation = 8.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.red_hat_logo),
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Reginalds Tower.",
                style = MaterialTheme.typography.bodyMedium
                //TODO
                // Add Save Icon and Implement
                // Ratings

            )
            Text(
                text = "Reginalds Tower.",
                style = MaterialTheme.typography.bodyMedium
                //TODO
                // Add Save Icon and Implement
                // Ratings

            )
        }
    }
}


@Preview
@Composable
fun TourComposablePreview(){
    TourComposable()
}
