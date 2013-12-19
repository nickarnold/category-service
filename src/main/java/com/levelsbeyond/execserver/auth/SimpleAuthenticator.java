package com.levelsbeyond.execserver.auth;

import com.google.common.base.Optional;
import com.levelsbeyond.execserver.core.Authentication;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

public class SimpleAuthenticator implements Authenticator<BasicCredentials, Authentication> {
	private String adminPassword;
	
	public SimpleAuthenticator(String adminPassword) {
		this.adminPassword = adminPassword;
	}	
	
    @Override
    public Optional<Authentication> authenticate(BasicCredentials credentials) throws AuthenticationException {
        if (adminPassword.equals(credentials.getPassword())) {
            return Optional.of(new Authentication());
        }
        return Optional.absent();
    }
}
