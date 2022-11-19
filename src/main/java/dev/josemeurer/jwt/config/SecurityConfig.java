package dev.josemeurer.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public InMemoryUserDetailsManager users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("jose")
                        .password("{noop}123456") //NoOpPasswordEncoder, codificador de senha que não faz nada e é útil para teste
                        .authorities("read")
                        .build()
        );
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(csrf -> csrf.disable()) //Desativar falsificação de solicitação entre sites (CSRF)
                .authorizeRequests(auth -> auth
                        .anyRequest().authenticated() //O usuário deve ser autenticado para qualquer solicitação no aplicativo
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //O Spring Security nunca criará um HttpSession e nunca o usará para obter o Security Context
                .httpBasic(Customizer.withDefaults()) //O suporte de autenticação básica HTTP do Spring Security é ativado por padrão. No entanto, assim que qualquer configuração baseada em servlet for fornecida, o HTTP Basic deverá ser fornecido explicitamente
                .build();
        //Nunca desative a proteção CSRF deixando o gerenciamento de sessão ativado! Fazer isso abrirá você para um ataque de falsificação de solicitação entre sites
    }
}
