package io.github.stscoundrel.revalidator.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor
import java.time.Clock
import java.util.ArrayDeque

class RateLimitInterceptor(
    private val maxRequests: Int,
    windowSeconds: Long,
    private val clock: Clock = Clock.systemUTC()
) : HandlerInterceptor {
    private val windowMillis: Long
    private val requests = ArrayDeque<Long>()

    init {
        require(maxRequests > 0) { "Rate limit must allow at least one request" }
        require(windowSeconds > 0 && windowSeconds <= Long.MAX_VALUE / 1000) {
            "Rate limit window must be a positive number of seconds representable in milliseconds"
        }
        windowMillis = windowSeconds * 1000
    }

    @Synchronized
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val now = clock.millis()
        while (requests.isNotEmpty() && now - requests.peekFirst() >= windowMillis) {
            requests.removeFirst()
        }

        if (requests.size >= maxRequests) {
            val remainingMillis = windowMillis - (now - requests.peekFirst())
            val retryAfterSeconds = (remainingMillis - 1) / 1000 + 1
            response.status = HttpStatus.TOO_MANY_REQUESTS.value()
            response.setHeader("Retry-After", retryAfterSeconds.toString())
            return false
        }

        requests.addLast(now)
        return true
    }
}
