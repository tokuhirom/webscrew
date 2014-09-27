package me.geso.webscrew.request.body;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

import lombok.NonNull;
import me.geso.webscrew.Parameters;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.map.MultiValueMap;

public class UrlEncoded {
	public static Parameters parseQueryString(String queryString,
			@NonNull String encoding) throws UnsupportedEncodingException {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final MultiValueMap<String, String> query =
				MapUtils.multiValueMap(new LinkedHashMap(), LinkedHashSet.class);
		if (queryString != null) {
			for (final String pair : queryString.split("&")) {
				final int eq = pair.indexOf("=");
				if (eq < 0) {
					// key with no value
					query.put(URLDecoder.decode(pair, encoding), "");
				} else {
					// key=value
					final String key = URLDecoder.decode(pair.substring(0, eq),
							encoding);
					final String value = URLDecoder.decode(
							pair.substring(eq + 1),
							encoding);
					query.put(key, value);
				}
			}
		}
		return new Parameters(query);
	}

}
