package io.github.stscoundrel.revalidator.security

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.HandlerInterceptor

class SecretAuthInterceptor : HandlerInterceptor {
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val secret = request.getParameter("secret")
        if (secret == null || secret != System.getenv("API_SECRET")) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }
        return true
    }
}
 