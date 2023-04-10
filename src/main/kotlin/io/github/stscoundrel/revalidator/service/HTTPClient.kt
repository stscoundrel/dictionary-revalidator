package io.github.stscoundrel.revalidator.service

import java.net.HttpURLConnection
import java.net.URL

import org.springframework.stereotype.Service

@Service
class HTTPClient(
) {
    fun get(url: String): Int {
        // For fucks sake, what is with the HTTP clients of JVM ecosystem.
        val connectionUrl = URL(url)
        val connection = connectionUrl.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connect()

        val status = connection.responseCode
        connection.disconnect()

        return status
    }
}