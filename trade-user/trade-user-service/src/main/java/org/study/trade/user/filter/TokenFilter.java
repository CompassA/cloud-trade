package org.study.trade.user.filter;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.study.trade.user.model.UserDTO;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * @author Tomato
 * Created on 2021.09.13
 */
public class TokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_HEADER = "token";

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenFilter(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        String token = httpServletRequest.getHeader(TOKEN_HEADER);
        if (StringUtils.isNotBlank(token)) {
            Object userDTO = redisTemplate.opsForValue().get(token);
            if (userDTO != null) {
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                ((UserDTO) userDTO).getNick(), null, Collections.emptyList())
                );
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
