package com.example.neurableble

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.neurableble.MainActivity.NavItem

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center)
    {
        // Label
        Text(modifier = Modifier.padding(20.dp),
            text = stringResource(R.string.neurable_test),
            fontSize = 40.sp,
            color = Color.White)

        // Container for buttons
        Row(horizontalArrangement = Arrangement.SpaceEvenly)
        {
            // Button to go to FocusScreen
            Button(modifier = Modifier
                .height(80.dp)
                .width(160.dp),
                onClick = {
                    navController.navigate(NavItem.Focus.route)
                })
            {
                Text(text = stringResource(R.string.focus_n_scoring),
                    fontSize = 24.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center)
            }

            Spacer(Modifier.size(10.dp))

            // Button to go to BLEScreen
            Button(modifier = Modifier
                .height(80.dp)
                .width(160.dp),
                onClick = {
                    navController.navigate(NavItem.BLE.route)
                })
            {
                Text(text = stringResource(R.string.ble_n_sample),
                    fontSize = 24.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center)
            }
        }
    }

    BackHandler {
        // TODO: Proper exit
    }
}