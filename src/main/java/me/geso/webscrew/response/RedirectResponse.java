package me.geso.webscrew.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import me.geso.webscrew.Headers;

/**
 * Response represents redirect.
 * 
 * @author tokuhirom
 *
 */
public class RedirectResponse implements WebResponse {

	private final String location;
	private final Headers headers;
	private final List<Cookie> cookies;

	public RedirectResponse(String location) {
		this.location = location;
		this.headers = new Headers();
		this.cookies = new ArrayList<>();
	}

	@Override
	public void write(HttpServletResponse response) throws IOException {
		for (final Cookie cookie : this.cookies) {
			response.addCookie(cookie);
		}
		for (final String name : headers.keySet()) {
			for (final String value : headers.getAll(name)) {
				response.addHeader(name, value);
			}
		}
		response.sendRedirect(location);
	}

	@Override
	public void addHeader(String name, String value) {
		this.headers.add(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		this.headers.set(name, value);
	}

	@Override
	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

}
