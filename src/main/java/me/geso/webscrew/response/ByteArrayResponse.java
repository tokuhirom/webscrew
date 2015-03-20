package me.geso.webscrew.response;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

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
	private final Headers headers = new Headers();

	private int status = 200;

	private final byte[] body;

	public ByteArrayResponse(int status, byte[] body) {
		if (body == null) {
			throw new NullPointerException("[ByteArrayResponse] Body is null");
		}
		this.status = status;
		this.body = body;
	}

	private final List<Cookie> cookies = new ArrayList<>();

	@Override
	public void write(HttpServletResponse response) throws IOException {
		response.setStatus(status);
		this.cookies.forEach(response::addCookie);
		for (final String key : getHeaders().keySet()) {
			for (final String value : getHeaders().getAll(key)) {
				response.addHeader(key, value);
			}
		}
		try (OutputStream os = response.getOutputStream()) {
			os.write(body);
		}
	}

	public void setContentType(String contentType) {
		getHeaders().add("Content-Type", contentType);
	}

	public void setContentLength(long length) {
		getHeaders().add("Content-Length", "" + length);
	}

	@Override
	public void addHeader(String name, String value) {
		getHeaders().add(name, value);
	}

	@Override
	public void setHeader(String name, String value) {
		getHeaders().set(name, value);
	}

	@Override
	public void addCookie(Cookie cookie) {
		this.cookies.add(cookie);
	}

	public Headers getHeaders() {
		return headers;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public byte[] getBody() {
		return Arrays.copyOf(body, body.length);
	}


}
