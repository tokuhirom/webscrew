package me.geso.webscrew.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.geso.webscrew.Headers;

/**
 * Web Response contains byte array as a body.
 * 
 * @author tokuhirom
 *
 */
@ToString
public class ByteArrayResponse implements WebResponse {
	@Getter
	@Setter
	Headers headers = new Headers();

	@Getter
	@Setter
	private int status = 200;

	@Getter
	@Setter
	private byte[] body;

	private final List<Cookie> cookies = new ArrayList<>();

	public void write(HttpServletResponse response) throws IOException {
		response.setStatus(status);
		for (Cookie cookie : this.cookies) {
			response.addCookie(cookie);
		}
		for (String key : headers.keySet()) {
			for (String value : headers.getAll(key)) {
				response.addHeader(key, value);
			}
		}
		try (OutputStream os = response.getOutputStream()) {
			os.write(body);
		}
	}

	public void setContentType(String contentType) {
		headers.add("Content-Type", contentType);
	}

	public void setContentLength(long length) {
		headers.add("Content-Length", "" + length);
	}

	@Override
	public void addHeader(String name, String value) {
		headers.add(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		headers.set(name, value);
	}

	@Override
	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}
}
