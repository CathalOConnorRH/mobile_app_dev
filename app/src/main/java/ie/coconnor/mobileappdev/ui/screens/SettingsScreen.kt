package ie.coconnor.mobileappdev.ui.screens

import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.MaterialTheme
import android.annotation.SuppressLint
import androidx.activity.viewModels
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ie.coconnor.mobileappdev.AuthViewModel
import ie.coconnor.mobileappdev.CustomAppBar
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.ui.login.SignUpScreen
import androidx.activity.viewModels
import ie.coconnor.mobileappdev.ui.navigation.Destinations

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel) {
//fun SettingsScreen() {
    var darkMode by remember { mutableStateOf(true) }

    Scaffold(
    ) //{ //paddingValues ->
    {
        Column(
            modifier = Modifier
                .fillMaxSize(),
//                .padding(paddingValues),
            //horizontalAlignment = Alignment.CenterHorizontally,
            //verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = " Settings",
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
                Spacer(modifier = Modifier.width(8.dp))
            }
            if(!DataProvider.isAuthenticated || DataProvider.isAnonymous) {

                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,

                    ) {
                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
//                        .background(Color.DarkGray.copy(alpha= 0.5f))
                            .padding(30.dp)
                            .background(
                                //brush = Brush.horizontalGradient(listOf(Color.DarkGray, Color.LightGray)),
                                color = Color.LightGray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(20.dp)
                            )
//                        .fillMaxWidth(0.8f)
                            .height(150.dp)

                    ) {
                        Column(
                            modifier = Modifier,
                        ) {

                            Row(
                                modifier = Modifier
                                    .padding(15.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {


                                Text(
                                    text = "Log in to manage your tours and easily plan your next tour",
                                    style = MaterialTheme.typography.bodyMedium,

                                    //                    textAlign = TextAlign.Start,
                                )
                            }
                            Row(
                                modifier = Modifier,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {


                                OutlinedButton(
                                    onClick = {
                                        navController.navigate(Destinations.LoginScreen.route)
                                    },
                                    modifier = Modifier
//                                .size(width = 300.dp, height = 50.dp)
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,

                                        )
                                ) {
//                        Image(
//                            painter = painterResource(id = R.drawable.ic_google_logo),
//                            contentDescription = "Sign Out"
//                        )

                                    Text(
                                        text = "Sign In",
                                        modifier = Modifier.padding(6.dp),
//                                color = Color.Black.copy(alpha = 0.5f)
                                        color = Color.Black.copy()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = " Theme",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Spacer(modifier = Modifier.width(28.dp))

                Text(
                    text = "Dark Mode",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Light
                )
                SwitchWithIconExample(

                )
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = " Permissions",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier,
                ) {

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Spacer(modifier = Modifier.width(28.dp))

                        Text(
                            text = "Some Permission",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                        SwitchWithIconExample(

                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(28.dp))

                        Text(
                            text = "Some Permission",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                        SwitchWithIconExample(

                        )
                    }
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Text to Speech Settings",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier,
                ) {

                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {

                        Spacer(modifier = Modifier.width(28.dp))

                        Text(
                            text = "Some Permission",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                        SwitchWithIconExample(

                        )
                    }
                    Row(
                        modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(28.dp))

                        Text(
                            text = "Some Permission",
                            modifier = Modifier
                                .padding(10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Light
                        )
                        SwitchWithIconExample(

                        )
                    }
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Account Settings",
                    modifier = Modifier
                        .padding(10.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                    ) {

//                        Spacer(modifier = Modifier.width(28.dp))
                        if(DataProvider.isAuthenticated) {

                        OutlinedButton(
                            onClick = {
                                authViewModel.signOut()
                            },
                            modifier = Modifier
//                                .size(width = 300.dp, height = 50.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,

                                )
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_google_logo),
                                contentDescription = "Sign Out"
                            )

                            Text(
                                text = "Sign Out",
                                modifier = Modifier.padding(6.dp),
//                                color = Color.Black.copy(alpha = 0.5f)
                                color = Color.Black.copy()
                            )
                            }
                        }
                    }
                }
            }
        }
    }
}






//            Button(
//                onClick = {
//
//                },
//                modifier = Modifier
//                    .size(width = 300.dp, height = 50.dp)
//                    .fillMaxWidth()
//                    .padding(horizontal = 16.dp),
//                shape = RoundedCornerShape(10.dp),
////                    colors = ButtonDefaults.buttonColors(
////                        containerColor = Color.White
////                    )
//            ) {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_google_logo),
//                    contentDescription = ""
//                )
//                Text(
//                    text = "Sign Out",
//                    modifier = Modifier.padding(6.dp),
////                        color = Color.Black.copy(alpha = 0.5f)
//                )
//            }

@Composable
fun SwitchWithIconExample() {
    var checked by remember { mutableStateOf(false) }

    Switch(
        checked = checked,
        onCheckedChange = {
            checked = it
        },
        thumbContent = if (checked) {
            {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    modifier = Modifier.size(SwitchDefaults.IconSize),
                )
            }
        } else {
            null
        }
    )
}

@Preview
@Composable
fun SettingScreenPreview() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()
    SettingsScreen(navController, authViewModel)
}

