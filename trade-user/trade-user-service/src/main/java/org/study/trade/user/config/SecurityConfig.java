package org.study.trade.user.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;
import org.study.trade.user.ErrorEnum;
import org.study.trade.user.constants.ApiPath;
import org.study.trade.user.filter.JwtRequestFilter;
import org.study.trade.user.filter.TokenFilter;
import org.study.trade.user.mapper.UserDataMapper;
import org.study.trade.user.service.JwtService;
import org.study.trade.user.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Tomato
 * Created on 2021.09.12
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDataMapper userDataMapper;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Bean
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserService userService() {
        return new UserService(userDataMapper, passwordEncoder());
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() throws Exception {
        return new JwtRequestFilter(jwtService, userService());
    }

    @Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter(redisTemplate);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().frameOptions().disable();
        http.cors();

        http.addFilterBefore(tokenFilter(), UsernamePasswordAuthenticationFilter.class);

        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers(ApiPath.USER_LOGIN, ApiPath.USER_REGISTER).permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(new TomatoEntryPoint())
                .and()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService())
                .passwordEncoder(passwordEncoder());
    }

    @Slf4j
    public static class TomatoEntryPoint implements AuthenticationEntryPoint {

        private final ObjectMapper objectMapper = new ObjectMapper();

        @Override
        public void commence(HttpServletRequest request,
                             HttpServletResponse response,
                             AuthenticationException e) throws IOException, ServletException {
            log.error("access denied", e);

            response.setContentType("application/json;charset=utf-8");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            PrintWriter writer = response.getWriter();
            writer.write(
                    objectMapper.writeValueAsString(
                            ErrorEnum.AUTHENTICATION_EXCEPTION.create(e.getMessage())
                    )
            );
            writer.flush();
            writer.close();
        }
    }
}
