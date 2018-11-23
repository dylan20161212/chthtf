package com.thtf.app.audit;

import org.springframework.boot.actuate.security.AbstractAuthenticationAuditListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;

public class MyAuthenticAulditListener extends AbstractAuthenticationAuditListener {

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		// TODO Auto-generated method stub
		System.out.println("soure come form: "+event.getSource().getClass().getName()+" and authentic!");
	}

}
