package ie.coconnor.mobileappdev.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ie.coconnor.mobileappdev.AuthViewModel
import ie.coconnor.mobileappdev.R
import ie.coconnor.mobileappdev.models.DataProvider
import ie.coconnor.mobileappdev.ui.navigation.Destinations
import ie.coconnor.mobileappdev.utils.UIThemeController


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun SettingsScreen(navController: NavHostController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val placeholder = R.drawable.vector

    Scaffold(
    )
    {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
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
                            .padding(30.dp)
                            .background(
                                color = Color.LightGray.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(20.dp)
                            )
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
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color.White,

                                        )
                                ) {
                                    Text(
                                        text = "Sign In",
                                        modifier = Modifier.padding(6.dp),
                                        color = Color.Black.copy()
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                val user = Firebase.auth.currentUser
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = " Welcome",
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "${DataProvider.getDisplayName(user)}",
                        modifier = Modifier
                            .padding(10.dp),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    println(DataProvider.getProfilePhoto(user))
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(DataProvider.getProfilePhoto(user))
                            .crossfade(true)
                            .diskCacheKey(DataProvider.getProfilePhoto(user))
                            .memoryCacheKey(DataProvider.getProfilePhoto(user))
                            .error(placeholder)
                            .fallback(placeholder)
                            .diskCachePolicy(CachePolicy.ENABLED)
                            .memoryCachePolicy(CachePolicy.ENABLED)
                            .build(),
                        placeholder = painterResource(placeholder),
                        contentDescription = "",
                        modifier = Modifier
                            .height(150.dp)
                            .clip(
                                RoundedCornerShape(
                                    topEnd = 80.dp,
                                    topStart = 80.dp,
                                    bottomEnd = 80.dp,
                                    bottomStart = 80.dp
                                )
                            )
                    )
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

                    val isDarkMode by UIThemeController.isDarkMode.collectAsState()

                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = {
                            if (it) {
                                println("The switch is checked.")
                                UIThemeController.updateUITheme(true)
//                            sharedPref.setDarkMode(true)

                            } else {
                                println("The switch is unchecked.")
                                UIThemeController.updateUITheme(false)
//                            sharedPref.setDarkMode(false)

                            }
                        },
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
                                text = "Allow Location Service",
                                modifier = Modifier
                                    .padding(10.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light
                            )

                            val foregroundServiceIsRunning = true
                            Switch(
                                checked = foregroundServiceIsRunning,
                                onCheckedChange = {
                                    if (it) {
                                        println("The switch is checked.")
                                    } else {
                                        println("The switch is unchecked.")
                                    }
                                },
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
                            SwitchWithIconExample {

                            }
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
                                text = "Auto Play",
                                modifier = Modifier
                                    .padding(10.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light
                            )
                            SwitchWithIconExample {
                            }
                        }
                        Row(
                            modifier = Modifier,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(28.dp))

                            Text(
                                text = "Text to Speech langauge",
                                modifier = Modifier
                                    .padding(10.dp),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Light
                            )
                            SwitchWithIconExample {
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Image caching",
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
                            OutlinedButton(
                                onClick = {
                                    context.imageLoader.diskCache?.clear()
                                    context.imageLoader.memoryCache?.clear()
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                                shape = RoundedCornerShape(10.dp),
                            ) {
                                Text(
                                    text = "Clear Image Cache",
                                    modifier = Modifier.padding(6.dp),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }
                if (DataProvider.isAuthenticated && !DataProvider.isAnonymous) {
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
                                if (DataProvider.isAuthenticated) {

                                    OutlinedButton(
                                        onClick = {
                                            authViewModel.signOut()
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp),
                                        shape = RoundedCornerShape(10.dp),
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_google_logo),
                                            contentDescription = "Sign Out"
                                        )

                                        Text(
                                            text = "Sign Out",
                                            modifier = Modifier.padding(6.dp),
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
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
fun SwitchWithIconExample(onCheckedChange: () -> Unit) {
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
    val context: CompositionLocalContext

    SettingsScreen(navController, authViewModel)
}

@Composable
fun RoundImage(
    image: Painter, modifier: Modifier = Modifier
) {
    Image(
        painter = image,
        contentDescription = "Profile image",
        modifier = modifier
            .aspectRatio(1f, matchHeightConstraintsFirst = true)
            .border(
                width = 6.dp, color = Color.White, shape = CircleShape
            )
            .padding(3.dp)
            .clip(CircleShape)
    )
}