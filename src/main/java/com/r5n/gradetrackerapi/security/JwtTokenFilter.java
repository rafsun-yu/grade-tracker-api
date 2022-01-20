package com.r5n.gradetrackerapi.security;

import com.r5n.gradetrackerapi.model.User;
import com.r5n.gradetrackerapi.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * This filter sets authentication based on the JWT cookie found in the request header.
 */
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = jwtUtils.getJwtFromCookies(request);

            if (jwt == null)
                throw new BadCredentialsException("Token is not present in the cookies.");

            if (!jwtUtils.validateJwtToken(jwt))
                throw new BadCredentialsException("Invalid token.");

            String userId = jwtUtils.getUserNameFromJwtToken(jwt);
            User user = userRepository.findById(userId).orElse(null);

            if (user == null)
                throw new BadCredentialsException("The token doesn't belong to a registered user.");

            PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(
                    user,
                    null
            );
            authentication.setAuthenticated(true);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        } catch (Exception ex) {
            SecurityContextHolder.getContext().setAuthentication(null);

        } finally {
            filterChain.doFilter(request, response);
        }
    }
}
