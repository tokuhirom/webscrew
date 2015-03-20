package me.geso.webscrew.request.auth;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * Basic authentication header's value entity.
 */
public class HttpBasicAuthUserPass {
	private final String userId;
	private final String password;

	/**
	 * Create new instance.
	 * 
	 * @param userId id
	 * @param password password
	 */
	public HttpBasicAuthUserPass(String userId, String password) {
		this.userId = userId;
		this.password = password;
	}

	/**
	 * Get user id.
	 */
	public String getUserId() {
		return this.userId;
	}

	/**
	 * Get password
	 */
	public String getPassword() {
		return this.password;
	}

	public String asHeader(Charset charset) {
		return "Basic "
				+ Base64.getEncoder().encodeToString(
						(this.userId + ":" + this.password).getBytes(charset));
	}
}
