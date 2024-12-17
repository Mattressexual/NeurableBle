package com.example.neurableble

import android.Manifest
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

class BLEViewModel(application: Application): AndroidViewModel(application) {
    @RequiresApi(Build.VERSION_CODES.S)
    private val permissionsS = arrayOf(
        Manifest.permission.BLUETOOTH_SCAN,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private val permissionsLegacy = arrayOf(
        Manifest.permission.BLUETOOTH,
        Manifest.permission.BLUETOOTH_ADMIN,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    private val bluetoothManager: BluetoothManager = application.getSystemService(Application.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothGatt: BluetoothGatt? = null

    private val seenDevices = mutableSetOf<String?>()
    val scanResults = MutableLiveData<List<ScanResult>>()
    val connectionStatus = MutableLiveData<String>()
    var selectedDevice: BluetoothDevice? = null

    private val scanCallback = object: ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)

            val address = result?.device?.address
            if (!seenDevices.contains(address)) {
                seenDevices.add(address)
                val results = scanResults.value?.toMutableList() ?: mutableListOf()
                result?.let { results.add(it) }
                scanResults.value = results
            }
        }
    }

    private val gattCallback = object: BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)

            val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
                ActivityCompat.checkSelfPermission(application, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
            else
                ActivityCompat.checkSelfPermission(application, Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    connectionStatus.postValue("Connected")
                    gatt?.discoverServices()
                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    connectionStatus.postValue("Disconnected")
                    bluetoothGatt = null
                }
            }
        }
    }

    fun startScan() {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            scanResults.value = emptyList()
            seenDevices.clear()
            bluetoothAdapter?.bluetoothLeScanner?.startScan(scanCallback)
        } else {
            Toast.makeText(getApplication(), "Permission denied", Toast.LENGTH_SHORT).show()
        }

        // TODO: Limit how long to scan for?
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    fun stopScan() {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_SCAN) == PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_ADMIN) == PackageManager.PERMISSION_GRANTED

        if (hasPermission)
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(scanCallback)
    }

    fun connectToDevice(device: BluetoothDevice) {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            selectedDevice = device
            bluetoothGatt = device.connectGatt(getApplication(), false, gattCallback)
        }
    }

    fun disconnectDevice() {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S)
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED
        else
            ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.BLUETOOTH) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            bluetoothGatt?.disconnect()
            bluetoothGatt?.close()
            bluetoothGatt = null
            connectionStatus.postValue("Disconnected")
        }
    }

    fun neededPermissions(): Array<String> {
        val permissions =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) permissionsS
            else permissionsLegacy
        return permissions
    }
}