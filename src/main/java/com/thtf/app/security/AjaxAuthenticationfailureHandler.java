package com.thtf.app.security;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AjaxAuthenticationfailureHandler extends SimpleUrlAuthenticationFailureHandler {
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		response.setStatus(401);
		OutputStream out = response.getOutputStream();
		Writer writer = new OutputStreamWriter(out);
		writer.write("i am authentic failed!");
		writer.flush();
		writer.close();
		out.close();
		//response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "unauthentic request!");
	}
}
