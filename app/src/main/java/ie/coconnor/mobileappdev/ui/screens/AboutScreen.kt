package ie.coconnor.mobileappdev.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ie.coconnor.mobileappdev.ui.component.CustomDialog
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.utils.SharedPref

@Composable
fun AboutScreen(
    navController: NavController,
    sharedPref: SharedPref
)
{
    val showDialog =  remember { mutableStateOf(false) }
    val trips = true

    if(showDialog.value)
        CustomDialog(value = "", title="Create a trip", setShowDialog = {
            showDialog.value = it
            sharedPref.setLocationToSearch(it.toString())
            navController.navigate(Destinations.LocationsScreen.route)
        }) {
            Log.i("HomePage","HomePage : $it")
        }

    Scaffold(
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Plan",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.Bold
                )

            }


            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Locations",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Icon(imageVector = Icons.Outlined.Favorite,
                    contentDescription = "Favourite")

                Text(
                    text = "Save your locations you'd like to visit",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Row(
                    modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(imageVector = Icons.Outlined.LocationOn,
                    contentDescription = "Show saves on map")
                Text(
                    text = "See your locations on a map",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ){
                OutlinedButton(
                    onClick = {
                        showDialog.value = true
                    },
                    modifier = Modifier
//                                .size(width = 300.dp, height = 50.dp)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(10.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color.White,
//
//                        )
                ) {
                    Text(
                        text = "+\tCreate a trip",
                        modifier = Modifier.padding(6.dp),
                        fontSize = 15.sp,
//                                color = Color.Black.copy(alpha = 0.5f)
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            }

        }

    }


@Preview
@Composable
fun AboutScreenPreview(
    navController: NavController = rememberNavController(),
    sharedPref: SharedPref? = null

) {
    if (sharedPref != null) {
        AboutScreen(navController, sharedPref)
    }
}
