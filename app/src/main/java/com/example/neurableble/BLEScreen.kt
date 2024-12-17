package com.example.neurableble

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.neurableble.MainActivity.NavItem

@Composable
fun BLEScreen(navController: NavHostController) {

    val viewModel: BLEViewModel = viewModel<BLEViewModel>()
    val context = LocalContext.current

    val scanText = stringResource(R.string.start_scan)
    val stopText = stringResource(R.string.stop_scanning)

    val deniedText = stringResource(R.string.permissions_denied)
    val scanningText = stringResource(R.string.scanning)
    val stoppingText = stringResource(R.string.stopping)

    var scanning by remember { mutableStateOf(false) }
    var btText by remember { mutableStateOf(scanText) }
    val scanResults by viewModel.scanResults.observeAsState(emptyList())

    var toast: Toast? = null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly)
        {
            Spacer(Modifier.size(40.dp))

            // BLE Sample Label
            Text(text = stringResource(R.string.ble_sample),
                fontSize = 40.sp,
                color = Color.White,
                textAlign = TextAlign.Center)

            // Start/Stop Scan Button
            Button(onClick = {
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN)
                    == PackageManager.PERMISSION_GRANTED) {
                    scanning = !scanning
                    if (scanning) {
                        viewModel.startScan()
                        btText = stopText

                        toast?.cancel()
                        toast = Toast.makeText(context, scanningText, Toast.LENGTH_SHORT)
                        toast?.show()
                    } else {
                        viewModel.stopScan()
                        btText = scanText

                        toast?.cancel()
                        toast = Toast.makeText(context, stoppingText, Toast.LENGTH_SHORT)
                        toast?.show()
                    }
                } else {
                    toast?.cancel()
                    toast = Toast.makeText(context, deniedText, Toast.LENGTH_SHORT)
                    toast?.show()
                }
            })
            {
                Text(text = btText, fontSize = 20.sp)
            }
            HorizontalDivider(thickness = 2.dp, color = Color.White)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)) {
            items(scanResults) { result ->
                val device = result.device
                Button(
                    modifier = Modifier
                        .padding(5.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(5.dp),
                    onClick = {
                        viewModel.stopScan()
                        viewModel.connectToDevice(device)
                    })
                {
                    val deviceInfo = stringResource(R.string.device_info, device.name, device.address)
                    Text(text = deviceInfo,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center)
                }
                Spacer(Modifier.size(2.dp))

            }
        }
    }

    BackHandler {
        viewModel.stopScan()
        viewModel.disconnectDevice()
        navController.navigate(NavItem.Home.route)
    }
}

@Preview
@Composable
fun PreviewBLEScreen() {
    val navController = rememberNavController()
    BLEScreen(navController)
}