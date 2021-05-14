package com.example.demo.configuration;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.example.demo.dto.UserAuthCheckDto;
import com.example.demo.dto.UserDetailsDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	ObjectMapper objectMapper;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());
		http.authorizeRequests()
		.antMatchers("/user/**").hasRole("USER")
		.and().formLogin()
		.successHandler(successHandler())
        .failureHandler(failureHandler());
		
		http.logout(logout -> logout
                .permitAll()
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.setContentType("application/json");
                    response.getWriter().append("{\"message\": \"You are logged out successfully\"}");
                }
            ));
	}
	
	private AuthenticationEntryPoint authenticationEntryPoint() {
		return new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Please login to continue.");
			}
		};
	} 
	
	private AuthenticationSuccessHandler successHandler() {
		return new AuthenticationSuccessHandler() {
			@Override
			public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
					Authentication authentication) throws IOException, ServletException {
				
				UserDetailsDto userDetails = (UserDetailsDto) authentication.getPrincipal();
				UserAuthCheckDto authCheckData = new UserAuthCheckDto(userDetails);
				
				String jsonResponse = objectMapper.writeValueAsString(authCheckData);
				
				response.setContentType("application/json");
				response.getWriter().append(jsonResponse);
				response.setStatus(200);
			}
		};
	}

	private AuthenticationFailureHandler failureHandler() {
	    return new AuthenticationFailureHandler() {
	    	private ObjectMapper objectMapper = new ObjectMapper();
			@Override
			public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
					org.springframework.security.core.AuthenticationException exception) throws IOException, ServletException {
				// TODO Auto-generated method stub
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
		        Map<String, Object> data = new HashMap<>();
		        data.put(
		          "timestamp", 
		          Calendar.getInstance().getTime());
		        data.put(
		          "exception", 
		          exception.getMessage());

		        response.getOutputStream()
		          .println(objectMapper.writeValueAsString(data));
			}
		};
	}
	
	@Bean
	public PasswordEncoder encoder() {
	    return new BCryptPasswordEncoder();
	}
}
