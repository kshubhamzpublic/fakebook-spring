package com.kshz.fakebookserver.jwt;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.kshz.fakebookserver.exceptions.AuthorizationException;
import com.kshz.fakebookserver.exceptions.ConfigurationException;

@Component
public class JWT implements Serializable {
	private static final long serialVersionUID = -3601125439988199132L;

	@Value("${jwt.secret}")
	private String secret;

	public String generateToken(String id, String name, String username, String email) {
		// putting all claims inside an object
		Map<String, Object> claims = new HashMap<>();
		claims.put("id", id);
		claims.put("username", username);
		claims.put("email", email);
		claims.put("name", name);

		return _generateToken(claims);
	}
	
	public String getIdFromToken(Map<String, Claim> claim) {
		return claim.get("id").asString();
	}

	public String getNameFromToken(Map<String, Claim> claim) {
		return claim.get("name").asString();
	}

	public String getEmailFromToken(Map<String, Claim> claim) {
		return claim.get("email").asString();
	}

	public String getUsernameFromToken(Map<String, Claim> claim) {
		return claim.get("username").asString();
	}

	public String getIdFromToken(String token) {
		return getClaim("id", token).asString();
	}

	public String getNameFromToken(String token) {
		return getClaim("name", token).asString();
	}

	public String getEmailFromToken(String token) {
		return getClaim("email", token).asString();
	}

	public String getUsernameFromToken(String token) {
		return getClaim("username", token).asString();
	}

	public Claim getClaim(String claim, String token) {
		Map<String, Claim> claims = getAllClaimsFromToken(token);
		return claims.get(claim);
	}

	protected Map<String, Claim> getAllClaimsFromToken(String token) {
		DecodedJWT decodedJWT = verifyToken(token);
		return decodedJWT.getClaims();
	}

	private DecodedJWT verifyToken(String token) {
		DecodedJWT jwt = null;

		JWTVerifier verifier = com.auth0.jwt.JWT.require(Algorithm.HMAC256(secret)).build();
		
		try {
			jwt = verifier.verify(token);
		} catch (JWTVerificationException e) {
			// invalid token
			throw new AuthorizationException("Invalid Token", "Couldn't verify token. Token modified/changed.");
		}

		return jwt;
	}

	private String _generateToken(Map<String, Object> payloadClaims) {
		String token = null;
		
		try {
			token = com.auth0.jwt.JWT.create().withPayload(payloadClaims).sign(Algorithm.HMAC256(secret));
		} catch (JWTCreationException e) {
			// Invalid Signing configuration / Couldn't convert Claims
			throw new ConfigurationException("Invalid Configuration",
					"Invalid configuration / Couldn't convert Claims");
		}

		return token;
	}

}
