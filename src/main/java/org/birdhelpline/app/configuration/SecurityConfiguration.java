package org.birdhelpline.app.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private DataSource dataSource;

	@Autowired
    private MySimpleUrlAuthenticationSuccessHandler successHandler;

	@Autowired
    private MyLogoutSuccessHandler logoutSuccessHandler;

	@Value("${queries.users-query}")
	private String usersQuery;
	
	@Value("${queries.roles-query}")
	private String rolesQuery;

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		auth.
			jdbcAuthentication()
				.usersByUsernameQuery(usersQuery)
				.authoritiesByUsernameQuery(rolesQuery)
				.dataSource(dataSource)
				.passwordEncoder(bCryptPasswordEncoder);

	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.
			authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/signIn").permitAll()
				.antMatchers("/login").permitAll()
                .antMatchers("/registration**").permitAll()
				.antMatchers("/forgotPassword").permitAll()
				.antMatchers("/profilePicUpload").permitAll()
				.antMatchers("/enableUser").permitAll()
				//.antMatchers("/css/**").permitAll()
				//.antMatchers("/img/**").permitAll()
				//.antMatchers("/js/**").permitAll()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/admin/**").hasAuthority("ADMIN").anyRequest()
				.authenticated().and().csrf().disable().formLogin()
                .loginPage("/signIn")
                .loginProcessingUrl("/login")
				.failureUrl("/signIn?error=true")
                .defaultSuccessUrl("/default",true)
				.usernameParameter("username")
				.passwordParameter("password")
                //.successHandler(successHandler)

				.and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
                //.logoutSuccessHandler(logoutSuccessHandler)
                .and().exceptionHandling()
				.accessDeniedPage("/access-denied");
			
	}
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web
	       .ignoring()
	       .antMatchers("/resources/**", "/static/**", "/css/**", "/js/**", "/img/**");
	}

}