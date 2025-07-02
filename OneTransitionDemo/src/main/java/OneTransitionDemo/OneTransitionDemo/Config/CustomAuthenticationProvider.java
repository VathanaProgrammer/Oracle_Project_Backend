package OneTransitionDemo.OneTransitionDemo.Config;


import OneTransitionDemo.OneTransitionDemo.Models.User;
import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password!"));
        System.out.println("LoginController: user found, generating token...");


        if(user == null) {
            System.out.println("User not found");
            throw new BadCredentialsException("User not found");
        }
        if(!passwordEncoder.matches(password, user.getPassword())){
            System.out.println("Invalid password");
            throw new BadCredentialsException("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(email, password, Collections.emptyList());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

