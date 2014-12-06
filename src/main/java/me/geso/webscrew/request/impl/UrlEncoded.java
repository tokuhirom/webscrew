package me.geso.webscrew.request.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import lombok.NonNull;
import me.geso.webscrew.Parameters;

/**
 * Interface is not good. The class like this should use standard collection instead of DefaultParameters.
 * If you want to use this, you can move this class to different package.
 */
@Deprecated
class UrlEncoded {
	static Parameters parseQueryString(String queryString,
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
