package com.kshz.fakebookserver.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.google.api.client.auth.openidconnect.IdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

@Service
public class GoogleTokenVerifer {
	
	@Value("${google.client.id}")
	private String CLIENT_ID;
	
	public void verifyToken(String idTokenString) {
		HttpTransport transport = new NetHttpTransport();
		JsonFactory jsonFactory = new GsonFactory();
		
		GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
				.setAudience(List.of(CLIENT_ID))
				.build();
		
		try {
			// verify token
			GoogleIdToken idToken = verifier.verify(idTokenString);
			
			// get payload
			Payload payload = idToken.getPayload();
			
			// Get profile information from payload
			String email = (String) payload.get("email");
			String name = (String) payload.get("name");
			String username = email.split("@")[0].trim();
		} catch (GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
