package com.thtf.app.audit;

import java.util.Optional;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

@Configuration
public class UserIDAuditorBean implements AuditorAware<String> {

	@Override
	public Optional<String> getCurrentAuditor() {
		SecurityContext ctx = SecurityContextHolder.getContext();
        if (ctx == null) {
            return null;
        }
        if (ctx.getAuthentication() == null) {
            return null;
        }
        if (ctx.getAuthentication().getPrincipal() == null) {
            return null;
        }
        User principal = (User) ctx.getAuthentication().getPrincipal();
        if (principal.getClass().isAssignableFrom(User.class)) {
        	
        	 return Optional.of(principal.getUsername());
        } else {
            return null;
        }
	}

}
