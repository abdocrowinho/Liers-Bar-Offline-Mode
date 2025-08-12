package com.example.domain.Utlites

import java.net.Inet4Address
import java.net.NetworkInterface

fun getMyIpAddress() : String{
    val networkInterface = NetworkInterface.getNetworkInterfaces()

    for (i in networkInterface) {
        val address = i.inetAddresses
        for (addr in address) {
            if (!addr.isLoopbackAddress && addr is Inet4Address) {
                return addr.hostAddress!!
            }
        }
    }
    return "0.0.0.0"
}