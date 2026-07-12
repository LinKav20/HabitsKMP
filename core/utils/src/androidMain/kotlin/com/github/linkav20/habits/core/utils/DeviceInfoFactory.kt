package com.github.linkav20.habits.core.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiInfo
import android.net.wifi.WifiManager
import android.os.Build
import android.provider.Settings.Secure
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.Inet4Address
import java.net.InetAddress
import java.net.NetworkInterface
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

public object DeviceInfoFactory {
    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var wifiManager: WifiManager
    private lateinit var contentResolver: ContentResolver

    public fun initialize(context: Context) {
        connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        contentResolver = context.contentResolver
    }

    @SuppressLint("HardwareIds")
    public fun getDeviceId(): String? {
        return Secure.getString(contentResolver, Secure.ANDROID_ID)
    }

    private val deviceNameSync: String
        get() = try {
            "${Build.BRAND} ${Build.MODEL}"
        } catch (_: Exception) {
            "unknown"
        }

    public fun getDeviceName(): String {
        return deviceNameSync
    }

    @SuppressLint("MissingPermission")
    private suspend fun getWifiInfo(): WifiInfo? {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            wifiManager.connectionInfo
        } else {
            suspendCoroutine { continuation ->
                val networkRequest = NetworkRequest
                    .Builder()
                    .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                    .build()

                val callback = object : ConnectivityManager.NetworkCallback() {
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities,
                    ) {
                        val transportInfo = networkCapabilities.transportInfo
                        if (transportInfo is WifiInfo) {
                            continuation.resume(transportInfo)
                        } else {
                            continuation.resume(null)
                        }
                        connectivityManager.unregisterNetworkCallback(this)
                    }

                    override fun onLost(network: Network) {
                        continuation.resume(null)
                        connectivityManager.unregisterNetworkCallback(this)
                    }
                }
                connectivityManager.registerNetworkCallback(networkRequest, callback)
            }
        }
    }

    private suspend fun getWifiIpAddress(): String? {
        return try {
            withContext(Dispatchers.IO) {
                InetAddress
                    .getByAddress(
                        getWifiInfo()?.ipAddress?.let {
                            ByteBuffer
                                .allocate(4)
                                .order(ByteOrder.LITTLE_ENDIAN)
                                .putInt(it)
                                .array()
                        },
                    ).hostAddress
            }
        } catch (_: Exception) {
            null
        }
    }

    public suspend fun getIpAddress(): String? {
        return withContext(Dispatchers.IO) {
            try {
                NetworkInterface
                    .getNetworkInterfaces()
                    .toList()
                    .flatMap { it.inetAddresses.toList() }
                    .firstOrNull { !it.isLoopbackAddress && it is Inet4Address }
                    ?.hostAddress
            } catch (_: Exception) {
                getWifiIpAddress()
            }
        }
    }
}
