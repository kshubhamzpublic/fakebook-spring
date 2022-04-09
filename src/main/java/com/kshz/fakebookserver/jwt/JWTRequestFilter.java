package com.kshz.fakebookserver.jwt;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth0.jwt.interfaces.Claim;
import com.kshz.fakebookserver.model.User;
import com.kshz.fakebookserver.service.UserService;

/**
 * The JwtRequestFilter extends the Spring Web Filter OncePerRequestFilter
 * class. For any incoming request, this Filter class gets executed. It checks
 * if the request has a valid JWT token. If it has a valid JWT Token, then it
 * sets the authentication in context to specify that the current user is
 * authenticated.
 * 
 * @author shubham kumar
 *
 */

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

	@Autowired
	private JWT jwt;

	@Autowired
	private UserService userService;

	String userId;
	String username;
	String email;
	String userDisplayName;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		final String requestTokenHeader = request.getHeader("authorization");

		if (requestTokenHeader != null) {
			Map<String, Claim> payload = jwt.getAllClaimsFromToken(requestTokenHeader);

			userId = jwt.getIdFromToken(payload);
			userDisplayName = jwt.getNameFromToken(payload);
			username = jwt.getUsernameFromToken(payload);
			email = jwt.getEmailFromToken(payload);
		}

		// if token is valid configure Spring Security to manually set
		// authentication
		if (userId != null) {
			Optional<User> user = userService.findById(userId);

			if (user.isPresent()) {
				request.setAttribute("userId", userId);
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
						userId, null, null);
				usernamePasswordAuthenticationToken
						.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				// After setting the Authentication in the context, we specify
				// that the current user is authenticated. So it passes the
				// Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}

		filterChain.doFilter(request, response);
	}

}
