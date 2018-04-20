package com.woongjin.concur;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * grant_type : password 타입인경우
	 * @param auth
	 * @throws Exception
	 */
	@Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        .withUser("concur").password("concur!23").roles("USER")
        .and()
        .withUser("admin").password("admin!23").roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		//.anonymous().disable()
		//.authorizeRequests().antMatchers("/system/**").access("hasRole('ROLE_USER')")
		.authorizeRequests().antMatchers("/**").permitAll();
	  	;
    }
}
