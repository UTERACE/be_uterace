package com.be_uterace.security;

import com.be_uterace.repository.UserRepository;
import com.be_uterace.utils.PasswordGeneratorEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private CustomUserDetailsService customUserDetailsService;
    private PasswordEncoder passwordEncoder;

    public JwtAuthenticationProvider(CustomUserDetailsService customUserDetailsService, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // In BasicController.login() method, we call authenticationManager.authenticate(token)
        // Then, Authentication Manager calls AuthenticationProvider's authenticate method.
        // Since JwtAuthenticationProvider is our custom authentication provider,
        // this method will be executed.
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        // Fetching user as wrapped with UserDetails object
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        // If user is not null, then we check if password matches
        if (userDetails != null){
            if (passwordEncoder.matches(password, userDetails.getPassword())){
                // if it matches, then we can initialize UsernamePasswordAuthenticationToken.
                // Attention! We used its 3 parameters constructor.
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
                return authenticationToken;
            }
        }
        throw new BadCredentialsException("Incorrect username or password");
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
