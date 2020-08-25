package tacos.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(
            @Qualifier("userRepositoryUserDetailsService") UserDetailsService userDetailsService
    ) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(
            AuthenticationManagerBuilder auth
    ) throws Exception {
        auth.userDetailsService(userDetailsService)
            .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                .antMatchers(HttpMethod.POST, "/api/ingredients")
                    .permitAll()
                .antMatchers("/design", "/orders")
                    // .hasRole("ROLE_USER")
                    // .access("hasRole('ROLE_USER')")
                    .permitAll()
                .antMatchers(HttpMethod.PATCH, "/ingredients")
                    .permitAll()
                .antMatchers("/**")
                    // .permitAll();
                    .access("permitAll")
            .and()
                .formLogin()
                    .loginPage("/login")
            .and()
                .httpBasic()
                    .realmName("Taco Cloud")
            .and()
                .logout()
                    .logoutSuccessUrl("/")
            .and()
                .csrf()
                    .ignoringAntMatchers("/h2-console/**", "/ingredients/**", "/design", "/orders/**", "/api/**")
            .and()
                .headers()
                    .frameOptions()
                        .sameOrigin();
    }

}
