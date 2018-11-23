package com.thtf.app.audit;

import org.springframework.boot.actuate.security.AbstractAuthorizationAuditListener;
import org.springframework.security.access.event.AbstractAuthorizationEvent;

public class MyAuthorizationAuditListener extends AbstractAuthorizationAuditListener {

	@Override
	public void onApplicationEvent(AbstractAuthorizationEvent event) {
		// TODO Auto-generated method stub
		System.out.println("soure come form: "+event.getSource().getClass().getName()+" and authorized!");
		
	}

}
