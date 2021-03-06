package me.geso.webscrew.response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import lombok.NonNull;
import lombok.ToString;
import me.geso.webscrew.Headers;

/**
 * The response object using callback. This class is useful for streaming
 * response like CSV.
 */
@ToString
public class CallbackResponse implements WebResponse {

	private final Callback callback;
	private final Headers headers;
	private final List<Cookie> cookies;
	private int statusCode;

	public CallbackResponse(@NonNull Callback callback) {
		this.statusCode = 200;
		this.callback = callback;
		this.headers = new Headers();
		this.cookies = new ArrayList<>();
	}

	@Override
	public void write(HttpServletResponse response) throws IOException {
		response.setStatus(this.statusCode);
		this.cookies.forEach(response::addCookie);
		for (final String name : headers.keySet()) {
			for (final String value : headers.getAll(name)) {
				response.addHeader(name, value);
			}
		}
		try {
			this.callback.call(response);
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void addHeader(String name, String value) {
		this.headers.add(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		this.headers.set(name, value);
	}

	@FunctionalInterface
	public static interface Callback {
		@SuppressWarnings("RedundantThrows")
		public void call(HttpServletResponse resp) throws Exception;
	}

	@Override
	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

	@Override
	public void setStatus(int statusCode) {
		this.statusCode = statusCode;
	}

}
