package io.github.stscoundrel.revalidator.security

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import java.time.Clock

class RateLimitInterceptorTests {
    private val clock = mock(Clock::class.java)
    private val handler = Any()

    @Test
    fun sharesLimitAcrossEndpointsAndClients() {
        `when`(clock.millis()).thenReturn(0L)
        val interceptor = RateLimitInterceptor(2, 60, clock)
        val first = MockHttpServletRequest("GET", "/api/revalidate/old-norse")
        first.remoteAddr = "192.0.2.1"
        val second = MockHttpServletRequest("GET", "/api/logs")
        second.remoteAddr = "192.0.2.2"

        assertTrue(interceptor.preHandle(first, MockHttpServletResponse(), handler))
        assertTrue(interceptor.preHandle(second, MockHttpServletResponse(), handler))
        val response = MockHttpServletResponse()
        assertFalse(interceptor.preHandle(first, response, handler))
        assertEquals(429, response.status)
        assertEquals("60", response.getHeader("Retry-After"))
    }

    @Test
    fun expiresRequestsIndividuallyWithoutExtendingWindowOnRejection() {
        val interceptor = RateLimitInterceptor(2, 60, clock)
        val request = MockHttpServletRequest()
        `when`(clock.millis()).thenReturn(0L)
        assertTrue(interceptor.preHandle(request, MockHttpServletResponse(), handler))
        `when`(clock.millis()).thenReturn(30_000L)
        assertTrue(interceptor.preHandle(request, MockHttpServletResponse(), handler))

        `when`(clock.millis()).thenReturn(59_001L)
        val blocked = MockHttpServletResponse()
        assertFalse(interceptor.preHandle(request, blocked, handler))
        assertEquals("1", blocked.getHeader("Retry-After"))

        `when`(clock.millis()).thenReturn(60_000L)
        assertTrue(interceptor.preHandle(request, MockHttpServletResponse(), handler))
        val stillBlocked = MockHttpServletResponse()
        assertFalse(interceptor.preHandle(request, stillBlocked, handler))
        assertEquals("30", stillBlocked.getHeader("Retry-After"))
    }

    @Test
    fun rejectsInvalidConfiguration() {
        assertThrows(IllegalArgumentException::class.java) { RateLimitInterceptor(0, 60) }
        assertThrows(IllegalArgumentException::class.java) { RateLimitInterceptor(1, 0) }
        assertThrows(IllegalArgumentException::class.java) { RateLimitInterceptor(1, -1) }
    }
}
