package com.example.backend.security;

import com.nimbusds.jose.shaded.json.JSONArray;
import com.nimbusds.jose.shaded.json.JSONObject;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .anyRequest().hasRole("prothetic_user"))
                .exceptionHandling().accessDeniedHandler(
                        (request, response, ex) -> {
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, ex.getMessage());
                        })
                .and()
                .cors(Customizer.withDefaults())
                .oauth2ResourceServer(resourceServerConfigurer -> resourceServerConfigurer
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())));
    }

    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        val converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {

        val converter = new JwtGrantedAuthoritiesConverter();

        return new Converter<>() {
            @Override
            public Collection<GrantedAuthority> convert(Jwt jwt) {

                Collection<GrantedAuthority> grantedAuthorities = converter.convert(jwt);

                if (jwt.getClaim("realm_access") != null) {
                    JSONObject realmAccess = jwt.getClaim("realm_access");

                    if (realmAccess.get("roles") != null) {
                        JSONArray roles = (JSONArray) realmAccess.get("roles");

                        val keycloakAuthorities = roles.stream()
                                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                .toList();

                        grantedAuthorities.addAll(keycloakAuthorities);
                    }
                }

                return grantedAuthorities;
            }
        };
    }

}

