package com.thtf.app.services;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.thtf.app.domains.MyAuth;
import com.thtf.app.domains.UserOrgnizationRole;
import com.thtf.app.repositories.UserOrgnizationRoleRepository;
import com.thtf.app.repositories.UserRepository;

@Component("userDetailsService")
public class MyUserDetailsServiceImpl implements UserDetailsService{

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		List auths = new ArrayList<>();
//		if(username.equals("admin")){
//			
//			return User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();
//		}else{
//			throw new RuntimeException("user not found!",new Throwable("user not found!"));
//		}
//	}
	
	 @Autowired
	 private UserRepository userRepository;
	 
	 @Autowired
	 private UserOrgnizationRoleRepository userOrgnizationRoleRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		com.thtf.app.domains.User user = this.userRepository.findByLogin(username).orElseThrow(()->{
			throw  new RuntimeException("user not found!",new Throwable("user not found!"));
		});
		
		List<UserOrgnizationRole> list = this.userOrgnizationRoleRepository.findByUserId(user.getId()).orElse(Collections.EMPTY_LIST);
		
		List<MyAuth> auths = list.stream().map(uor->uor.getMyAuth()).collect(Collectors.toList());
		
		
		return User.builder().username(username).password(user.getPassword()).roles(null).authorities(auths).build();
		
		
		
//		List auths = new ArrayList<>();
//		if(username.equals("admin")){
//			
//			return User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();
//		}else{
//			throw new RuntimeException("user not found!",new Throwable("user not found!"));
//		}
	}

}
