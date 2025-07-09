package com.example.lumiai.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.Flow

object NetworkObserver {

    fun observe(context: Context): Flow<Boolean> = callbackFlow {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }
            override fun onLost(network: Network) {
                trySend(false)
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)

        // Send initial state
        val current = connectivityManager.activeNetwork != null
        trySend(current)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }
}
