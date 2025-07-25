package OneTransitionDemo.OneTransitionDemo.Config;

import OneTransitionDemo.OneTransitionDemo.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import OneTransitionDemo.OneTransitionDemo.Services.JWTService;
import java.util.Arrays;
import OneTransitionDemo.OneTransitionDemo.Config.CustomHandshakeHandler;
import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;

    @Value("${upload.path.profile}")
    private String updatedProfilePath;

    @Autowired
    private CustomAuthenticationProvider customAuthenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(withDefaults())     // enable CORS using above config
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/user/register","/api/user/login","/api/uploads/profile/**", "/upload/**").permitAll()
                        .requestMatchers("/me", "/api/user/**","/api/user/logout" , "/chat/**","/api/exams/**", "/api/messages/**").authenticated()
                        .requestMatchers("/api/actions/**", "/api/assignments/**", "/api/students/**").authenticated()
                        .requestMatchers("/api/beforeDetail/**", "/api/session-logs/**").authenticated()
                        .anyRequest().authenticated()
                )
                .authenticationProvider(customAuthenticationProvider)
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://localhost:3000",
                "http://localhost:5174",
                "http://192.168.18.61:5173",
                "http://172.20.10.2:5173",
                "http://192.168.12.174:5173",
                "https://bxvl7j9c-5173.asse.devtunnels.ms/"
        ));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/api/upload/profile/**")
                .addResourceLocations("file:" + updatedProfilePath + "/");
    }

}
