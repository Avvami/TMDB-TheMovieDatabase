package com.personal.tmdb.core.domain.util

import okhttp3.Dns
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import java.net.InetAddress

class CustomDns: Dns {

    private val bootstrapClient = OkHttpClient.Builder().build()

    private val quad8Dns = DnsOverHttps.Builder()
        .client(bootstrapClient)
        .url("https://dns.quad9.net/dns-query".toHttpUrl())
        .bootstrapDnsHosts(
            InetAddress.getByName("9.9.9.9"),
            InetAddress.getByName("149.112.112.112")
        )
        .build()

    override fun lookup(hostname: String): List<InetAddress> {
        return try {
            quad8Dns.lookup(hostname)
        } catch (e: Exception) {
            println("Quad9 DNS failed for $hostname: ${e.message}")
            Dns.SYSTEM.lookup(hostname)
        }
    }
}