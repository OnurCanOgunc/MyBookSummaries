package com.decode.mybooksummaries.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import androidx.core.content.getSystemService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

class AndroidConnectivityObserver@Inject constructor(
    @ApplicationContext context: Context
) : ConnectivityObserver {

    private val connectivityManager = context.getSystemService<ConnectivityManager>()

    override val isConnected: Flow<Boolean>
        get() = callbackFlow {
            val callback = object : NetworkCallback() {
                var lastConnected = false
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Log.d("onAvailable", "true")
                    trySend(true)
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Log.d("onLost", "true")
                    if (lastConnected) {
                        lastConnected = false
                        trySend(false)
                    }
                }

                override fun onUnavailable() {
                    super.onUnavailable()
                    Log.d("onUnavailable", "true")
                    trySend(false)
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    super.onCapabilitiesChanged(network, networkCapabilities)
                    val connected = networkCapabilities.hasCapability(
                        NetworkCapabilities.NET_CAPABILITY_VALIDATED
                    )
                    Log.d("onCapabilitiesChanged", connected.toString())
                    if (connected != lastConnected) {
                        lastConnected = connected
                        trySend(connected)
                    }
                }
            }
            connectivityManager?.registerDefaultNetworkCallback(callback)
            awaitClose { connectivityManager?.unregisterNetworkCallback(callback) }
        }.distinctUntilChanged()
}