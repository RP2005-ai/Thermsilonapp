package com.roheet.thermsilon.ui.theme

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.IntentFilter
import android.widget.Toast

class BluetoothManagerHelper(private val context: Context) {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

    fun isBluetoothSupported(): Boolean {
        return bluetoothAdapter != null
    }

    @SuppressLint("MissingPermission")
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter?.isEnabled == true
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): Set<BluetoothDevice>? {
        return bluetoothAdapter?.bondedDevices
    }

    @SuppressLint("MissingPermission")
    fun discoverNearbyDevices(callback: (BluetoothDevice) -> Unit) {
        bluetoothAdapter?.startDiscovery()

        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: android.content.Intent?) {
                if (BluetoothDevice.ACTION_FOUND == intent?.action) {
                    @Suppress("DEPRECATION")
                    val device: BluetoothDevice? =
                        intent?.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)

                    device?.let {
                        Toast.makeText(context, "Found: ${it.name ?: "Unknown Device"}", Toast.LENGTH_SHORT).show()
                        callback(it)
                    }
                }
            }
        }

        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        context.registerReceiver(receiver, filter)
    }

    fun stopDiscovery() {
        bluetoothAdapter?.cancelDiscovery()
    }
}
