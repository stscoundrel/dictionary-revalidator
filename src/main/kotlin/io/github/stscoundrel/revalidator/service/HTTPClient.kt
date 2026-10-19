package io.github.stscoundrel.revalidator.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.net.HttpURLConnection
import java.net.URL

@Service
class HTTPClient(
    @Value("\${revalidator.http.connect-timeout-ms:10000}") private val connectTimeoutMillis: Int,
    @Value("\${revalidator.http.read-timeout-ms:120000}") private val readTimeoutMillis: Int
) {
    init {
        require(connectTimeoutMillis > 0) { "HTTP connection timeout must be positive" }
        require(readTimeoutMillis > 0) { "HTTP read timeout must be positive" }
    }

    fun get(url: String): Int {
        // For fucks sake, what is with the HTTP clients of JVM ecosystem.
        val connectionUrl = URL(url)
        val connection = connectionUrl.openConnection() as HttpURLConnection
        try {
            connection.requestMethod = "GET"
            connection.connectTimeout = connectTimeoutMillis
            connection.readTimeout = readTimeoutMillis
            connection.connect()
            return connection.responseCode
        } finally {
            connection.disconnect()
        }
    }
}
