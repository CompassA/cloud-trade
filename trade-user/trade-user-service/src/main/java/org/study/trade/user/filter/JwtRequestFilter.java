package org.study.trade.user.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.study.trade.user.model.UserDTO;
import org.study.trade.user.service.JwtService;
import org.study.trade.user.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Tomato
 * Created on 2021.09.12
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final JwtService jwtService;
    private final UserService userService;

    public JwtRequestFilter(JwtService jwtService,
                            UserService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String jwt = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.isNotBlank(jwt)) {
            UserDTO userInfo = jwtService.getUserInfo(jwt);
            if (userInfo != null) {
                UserDetails userDetails = userService.loadUserByUsername(userInfo.getNick());
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                userDetails.getUsername(), null, Collections.emptyList())
                );
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
