package com.thtf.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.thtf.app.filters.CustomAuthenticationFilter;
import com.thtf.app.security.AjaxAuthenticationSuccessHandler;
import com.thtf.app.security.AjaxAuthenticationfailureHandler;
import com.thtf.app.security.AjaxLogoutSuccessHandler;
import com.thtf.app.security.RestAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {
		 
	    @Override
	    protected void configure(HttpSecurity http) throws Exception {
//	        http.authorizeRequests()
////	        .antMatchers("/api/authentic")
////	        .permitAll()
//	        .anyRequest().authenticated()
//	        .and()
//	        .formLogin().loginProcessingUrl("/api/authentic")
//	        .successHandler(this.authicSuccessHandler())
//	        .failureHandler(this.authicFailureHandler())
//	        .permitAll()
//	        .and()
//	        .logout().logoutSuccessHandler(this.ajaxLogoutSuccessHandler())
//	        .and()
//	        .exceptionHandling().authenticationEntryPoint(this.restAuthenticationEntryPoint())
//	        .and()
//	        .csrf().disable();
	    	
	    	
	    	http
            .cors().and()
            .antMatcher("/**").authorizeRequests()
            .antMatchers("/", "/api/authentic").permitAll()
//            .anyRequest().permitAll()
            .anyRequest().authenticated()
            //这里必须要写formLogin()，不然原有的UsernamePasswordAuthenticationFilter不会出现，也就无法配置我们重新的UsernamePasswordAuthenticationFilter
            .and().formLogin()
            
            .and().exceptionHandling().authenticationEntryPoint(this.restAuthenticationEntryPoint());

	        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class).csrf().disable();
	        
	    }
	    
	    
	  //注册自定义的UsernamePasswordAuthenticationFilter
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(this.authicSuccessHandler());
        filter.setAuthenticationFailureHandler(this.authicFailureHandler());
        filter.setFilterProcessesUrl("/api/authentic");

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

	 
	    
    @Bean
    public AuthenticationEntryPoint restAuthenticationEntryPoint() {
        return new RestAuthenticationEntryPoint();
    }



	
    @Bean
    public AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler() {
        return new AjaxLogoutSuccessHandler();
    }
	
	@Bean
	public AjaxAuthenticationSuccessHandler authicSuccessHandler(){
		return new AjaxAuthenticationSuccessHandler();
	}
	
	@Bean
	public AjaxAuthenticationfailureHandler authicFailureHandler(){
		return new AjaxAuthenticationfailureHandler();
	}
	
//    @Bean
//    public UserDetailsService userDetailsServices() throws Exception {
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build());
//        manager.createUser(User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build());
//        return manager;
//    }

}
