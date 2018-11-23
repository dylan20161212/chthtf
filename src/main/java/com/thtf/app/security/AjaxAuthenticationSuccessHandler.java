package com.thtf.app.security;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


public class AjaxAuthenticationSuccessHandler implements AuthenticationSuccessHandler  {
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		OutputStream out = response.getOutputStream();
		Writer writer = new OutputStreamWriter(out);
		writer.write("i am authentic successfully!");
		writer.flush();
		writer.close();
		out.close();
		response.setStatus(HttpServletResponse.SC_OK);
	}
}
