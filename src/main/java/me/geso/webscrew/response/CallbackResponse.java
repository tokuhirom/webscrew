package me.geso.webscrew.response;

import java.io.IOException;

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

	public CallbackResponse(@NonNull Callback callback) {
		this.callback = callback;
		this.headers = new Headers();
	}

	@Override
	public void write(HttpServletResponse response) throws IOException {
		for (String name : headers.keySet()) {
			for (String value : headers.getAll(name)) {
				response.addHeader(name, value);
			}
		}
		try {
			this.callback.call(response);
		} catch (Exception e) {
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
		public void call(HttpServletResponse resp) throws Exception;
	}

}
