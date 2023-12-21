package com.be_uterace.config;

import com.be_uterace.payload.response.ResponseObject;
import com.be_uterace.security.JwtAuthenticationEntryPoint;
import com.be_uterace.security.JwtAuthenticationFilter;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
@EnableMethodSecurity
//(securedEnabled = true,
//jsr250Enabled = true,
//prePostEnabled = true) // by default
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint authenticationEntryPoint;

    private JwtAuthenticationFilter authenticationFilter;

    public SecurityConfig(UserDetailsService userDetailsService,
                          JwtAuthenticationEntryPoint authenticationEntryPoint,
                          JwtAuthenticationFilter authenticationFilter){
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
        this.authenticationFilter = authenticationFilter;
    }

    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((requests) ->requests
                        .requestMatchers("/images/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/clubs", "/api/events", "/api/news","/api/clubs/**","/api/events/**",
                                "/api/news/**","/api/area/**","/api/home", "/api/scoreboard"
                                , "/api/user/recent-active/**","/api/decode-polyline","/api/strava/status",
                                "/api/webhook/**","/api/user/{user_id}","/api/user/event-completed/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth","/api/auth/**","/api/webhook/**", "/api/re-initialize/**").permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/news/**","/api/clubs/**","/api/events/join-event/**","/api/events/leave-event/**"
                                ,"/api/strava/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/news/**","/api/clubs/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/news/**","/api/clubs/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/events").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/events/*/add-distance/**").hasAnyRole("ADMIN")

                        .requestMatchers(HttpMethod.PUT,"/api/events/**").hasAnyRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/events/**").hasAnyRole("ADMIN")
                        .requestMatchers("/api/user").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/api/admin", "/api/manage-news", "/api/manage-club", "/api/manage-user", "/api/manage-event",
                                "/api/distance", "/api/manage-news/**", "/api/manage-club/**", "/api/manage-user/**", "/api/manage-event/**",
                                "/api/distance/**").hasRole("ADMIN")


                        .anyRequest().authenticated()

                ).exceptionHandling( exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler())
                ).sessionManagement( session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(corsFilter(), ChannelProcessingFilter.class);
        http.cors(Customizer.withDefaults());
        return http.build();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
//    private OncePerRequestFilter corsFilter() {
//        return new OncePerRequestFilter() {
//            @Override
//            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//                response.addHeader("Access-Control-Allow-Origin", "*");
//                response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
//                response.addHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
//                response.addHeader("Access-Control-Max-Age", "3600");
//                filterChain.doFilter(request, response);
//            }
//        };
//    }
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            final Map<String, Object> body = new LinkedHashMap<>();

            body.put("status", HttpServletResponse.SC_FORBIDDEN);
            body.put("message", "Access Denied! You don't have permission to access this resource.");

            final ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(response.getOutputStream(), body);
        };
    }
}