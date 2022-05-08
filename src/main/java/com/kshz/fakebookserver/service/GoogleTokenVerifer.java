package com.kshz.fakebookserver.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.kshz.fakebookserver.exceptions.AuthorizationException;

@Component
public class GoogleTokenVerifer {
	
	@Value("${google.client.id}")
	private String CLIENT_ID;
	
	private GoogleIdTokenVerifier verifier;
	
	public boolean isTokenValid(String idTokenString) {
		HttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = new GsonFactory();
		
		verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(List.of(CLIENT_ID))
				.build();
		// verify token
		try {
			GoogleIdToken idToken = verifier.verify(idTokenString);
			return idToken != null;
		} catch (GeneralSecurityException e) {
			// e.printStackTrace();
			return false;
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
	}
	
	public Payload getPayload(String idTokenString) {
		HttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = new GsonFactory();
		
		verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(List.of(CLIENT_ID))
				.build();
		
		Payload payload = null;
		try {
			// verify token
			GoogleIdToken idToken = verifier.verify(idTokenString);
			
			if (idToken == null) {
				throw new AuthorizationException("Identity cannot be verified.", "Unknown Identity or token expired.");
			}
			
			// get payload
			payload = idToken.getPayload();
		} catch (GeneralSecurityException e) {
			// e.printStackTrace();
			throw new AuthorizationException("Identity cannot be verified.", "Unknown Identity or token expired.");
		} catch (IOException e) {
			// e.printStackTrace();
			throw new AuthorizationException("Identity cannot be verified.", "Unknown Identity or token expired.");
		}
		
		return payload;
	}
}
