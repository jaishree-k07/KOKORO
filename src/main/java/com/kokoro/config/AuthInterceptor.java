package com.kokoro.config;

import com.kokoro.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/** Every /api request (except /api/auth) must carry "Authorization: Bearer <token>". */
@Component
public class AuthInterceptor implements HandlerInterceptor {
    private final UserRepository users;

    public AuthInterceptor(UserRepository users) { this.users = users; }

    @Override
    public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object handler) throws Exception {
        String h = req.getHeader("Authorization");
        if (h != null && h.startsWith("Bearer ")) {
            var user = users.findByToken(h.substring(7));
            if (user.isPresent()) {
                req.setAttribute("user", user.get());
                return true;
            }
        }
        res.setStatus(401);
        res.setContentType("application/json");
        res.getWriter().write("{\"error\":\"Please log in\"}");
        return false;
    }
}
