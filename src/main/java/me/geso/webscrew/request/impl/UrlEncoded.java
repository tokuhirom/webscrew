package me.geso.webscrew.request.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import lombok.NonNull;

class UrlEncoded {
	static DefaultParameters parseQueryString(String queryString,
			@NonNull String encoding) throws UnsupportedEncodingException {
		final DefaultParameters.Builder builder = DefaultParameters.builder();
		if (queryString != null) {
			for (final String pair : queryString.split("&")) {
				final int eq = pair.indexOf("=");
				if (eq < 0) {
					// key with no value
					builder.put(URLDecoder.decode(pair, encoding), "");
				} else {
					// key=value
					final String key = URLDecoder.decode(pair.substring(0, eq),
							encoding);
					final String value = URLDecoder.decode(
							pair.substring(eq + 1),
							encoding);
					builder.put(key, value);
				}
			}
		}
		return builder.build();
	}

}
