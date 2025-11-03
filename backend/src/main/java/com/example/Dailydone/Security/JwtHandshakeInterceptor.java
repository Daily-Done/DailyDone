package com.example.Dailydone.Security;

import com.example.Dailydone.Service.UserAuthServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import jakarta.servlet.http.Cookie;
import java.security.Principal;
import java.util.Map;

@Configuration
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserAuthServices userAuthServices;
    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        ServletServerHttpRequest servlet = (ServletServerHttpRequest) request;

        // 1) Try Authorization header (works for non-browser clients)
        String auth = servlet.getServletRequest().getHeader("Authorization");
        String token = (auth != null && auth.startsWith("Bearer ")) ? auth.substring(7) : null;

        // 2) Fallback: query param ?token=JWT (works in browser)
        if (token == null) token = servlet.getServletRequest().getParameter("token");

        // 3) Optional fallback: cookie named "token"
        if (token == null && servlet.getServletRequest().getCookies() != null) {
            for (Cookie c : servlet.getServletRequest().getCookies()) {
                if ("token".equals(c.getName())) {
                    token = c.getValue();
                    break;
                }
            }
        }

        if (token != null && !token.isBlank()) {
            String username = jwtUtil.extractUsername(token);
            UserDetails user = userAuthServices.loadUserByUsername(username);

            //  Store a real Principal/Authentication, not UserDetails
            UsernamePasswordAuthenticationToken principal =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());

            attributes.put("principal", principal);
        }
        return true; // or return false to reject missing/invalid token
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }
}