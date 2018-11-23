package com.thtf.app.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component("userDetailsService")
public class MyUserDetailsServiceImpl implements UserDetailsService{

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List auths = new ArrayList<>();
		if(username.equals("admin")){
			
			return User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();
		}else{
			throw new RuntimeException("user not found!",new Throwable("user not found!"));
		}
	}

}
