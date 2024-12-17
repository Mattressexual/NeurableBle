package com.example.neurableble

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.neurableble.MainActivity.NavItem
import com.example.neurableble.ui.theme.CustomGreen
import com.example.neurableble.ui.theme.CustomRed

@Composable
fun FocusScreen(navController: NavHostController) {
    val viewModel: FocusViewModel = viewModel<FocusViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val score = uiState.score

    // Values for Button appearance update
    val startText = stringResource(R.string.start)
    val stopText = stringResource(R.string.stop)
    val startColor = CustomGreen
    val stopColor = CustomRed

    var btText by remember { mutableStateOf(startText) }
    var btColor by remember { mutableStateOf(startColor) }
    var scoring by remember { mutableStateOf(false) }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        // Text label
        Text(text = stringResource(R.string.focus_score),
            fontSize = 40.sp,
            textAlign = TextAlign.Center,
            color = Color.White)

        // Text to show score
        Text(modifier = Modifier.padding(50.dp),
            text = score.toString(),
            fontSize = 80.sp,
            color = Color.White)

        // Start/Stop button
        Button(modifier = Modifier.size(180.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = btColor),
            onClick = {
                scoring = !scoring
                if (scoring) {
                    viewModel.startScoring()
                    btText = stopText
                    btColor = stopColor
                } else {
                    viewModel.stopScoring()
                    btText = startText
                    btColor = startColor
                }
            })
        {
            Text(text = btText,
                fontSize = 50.sp,
                textAlign = TextAlign.Center)
        }
    }

    BackHandler {
        viewModel.stopScoring()
        navController.navigate(NavItem.Home.route)
    }
}